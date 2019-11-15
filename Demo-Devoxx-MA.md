# Demo Devoxx MA

> Les scripts sont dans le répertoire `demo`.
> Quand on démarre une image de Pulsar depuis le début, il faut réinstaller le nar du sink Elasticvsearch
> * se connecter sur le container
> * copier le nar dans /pulsar/connectors
> * relancer l'image

## ######################################
## 1ère etape: la base
## ######################################


```bash
cd ~/devoxxma/demo

./01-start-pulsar-standalone.sh

pulsar-admin clusters list
pulsar-admin brokers list standalone
pulsar-admin brokers healthcheck
pulsar-admin brokers get-internal-config | jq
```

## ###################################################################
## 2ème étape: lancement de clients CLI (producteur et consommateur)
## ###################################################################


* Consommation des messages
```bash
pulsar-client consume \
    -n 0 -r 1 \
    -s "demo-subs-exclusive" \
    -t Exclusive \
    demo-topic
```

* Production des messages

```bash
pulsar-client produce \
    -m "Hello Morocco !!" \
    -n 100 -r 1 \
    demo-topic
```


## #########################################
## 3ème étape: création tenant / namespace
## #########################################


* Création tenant et namespace

```bash
pulsar-admin tenants list
pulsar-admin tenants create demo
pulsar-admin namespaces create demo/ecommerce
pulsar-admin namespaces list demo
```

## ############################################################
## 4ème étape:  debut ECOMMERCE - basic producer avec schema
## ############################################################


* Lancement de OrderGenerator

* Consommation des messages
```bash
pulsar-client consume -n 0 \
  -s "demo-orders-all-exclusive" \
  -t Exclusive \
  demo/ecommerce/orders-all
```

* Voir le schema utilisé
```bash
pulsar-admin schemas get demo/ecommerce/orders-all | jq -r '.schema' #| base64 -D | jq
```

## ######################################
## 5ème étape - fonction de filtrage
## ######################################


* Lancement de la fonction en mode local
```bash
JAR=${HOME}/devoxxma/examples/target/pulsar-examples-jar-with-dependencies.jar

pulsar-admin functions localrun \
   --jar ${JAR} \
   --className io.millesabords.pulsar.examples.ecommerce.FilterByCountryFunction \
   --fqfn talk/demo/orderfilterbycountry \
   --inputs persistent://talk/demo/orders-all \
   --output persistent://talk/demo/orders-but-us \
   --log-topic persistent://talk/demo/orders-logs \
   --user-config '{dbpath: "'$(pwd)'/GeoLite2-Country.mmdb"}'

#   --state-storage-service-url bk://localhost:4181
```


* (DEPRECATED) Lecture du state
```bash
#pulsar-admin functions querystate --fqfn talk/demo/orderfilter -k average-amount -w
```

* Lecture du topic avec les messages filtrés
```bash
pulsar-client consume \
    -n 0 -r 1 \
    -s "demo-ecommerce-orders-us-exclusive" \
    -t Exclusive \
    demo/ecommerce/orders-us 
```

* Lecture du topic avec les logs
```bash
pulsar-client consume -n 0 \
  -s "demo-fct-log-exclusive" \
  -t Exclusive \
  talk/demo/orders-logs
```

## ######################################
## 6ème étape - pulsar io elasticsearch
## ######################################

* Lancement d'elastic + kibana

```bash
cd /path/to/talk-intro-apache-pulsar
docker-compose start #up #start
```

* Deploiement du connector
```bash
# Create sink
cd /path/to/talk-intro-apache-pulsar

pulsar-admin sink create \
      --tenant talk \
      --namespace demo \
      --name elasticsearch-sink \
      --sink-type elastic_search \
      --sink-config-file $(pwd)/elasticsearch-sink.yml \
      --inputs talk/demo/orders-us
```

* Controle des connecteurs
```bash
# check connectors
curl -s http://localhost:8080/admin/v2/functions/connectors | jq
[
  {
    "name": "elastic_search",
    "description": "Writes data into Elastic Search",
    "sinkClass": "org.apache.pulsar.io.elasticsearch.ElasticSearchSink"
  }
]
```

* Relancer l'OrderGenerator

* Aller sur localhost:5601

## ######################################
## 7ème étape - SQL
## ######################################

```sql
show schemas in pulsar;
show tables in pulsar."demo/ecommerce";

select id, orderdate, ipaddress, amount, email 
        from pulsar."demo/ecommerce"."orders-all";
```

```bash
# In case, you use a strange JVM (for example, AdoptOpneJDK)
export JAVA_TOOL_OPTIONS="-Djava.vendor='Oracle Corporation'"
pulsar sql-worker run
```
