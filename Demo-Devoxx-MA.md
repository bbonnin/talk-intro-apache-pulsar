# Demo Devoxx MA

## Premières etapes

> Montrer les outils en ligne de commande

* pulsar
* pulsar-admin
* pulsar-client

## Run

* Démarrage en mode standalone
```bash
# pulsar standalone

pulsar-admin clusters list
pulsar-admin brokers list standalone
pulsar-admin brokers healthcheck
pulsar-admin brokers get-internal-config
```

* Création tenant et namespace

```bash
pulsar-admin tenants create talk
pulsar-admin namespaces create talk/demo
```

## Test 1 (basic producer avec schema)

* Lancement de OrderGenerator

* Consommation des messages
```bash
pulsar-client consume -n 0 \
  -s "demo-fct-output-exclusive" \
  -t Exclusive \
  talk/demo/orders-all
```

* Voir le schema utilisé
```bash
pulsar-admin schemas get talk/demo/orders-all | jq -r '.schema' #| base64 -D | jq
```


## Test 2 - fonction de filtrage

* Lancement de la fonction en mode local
```bash
JAR=${HOME}/Documents/Perso/Dev/talk-intro-apache-pulsar/examples/target/pulsar-examples-jar-with-dependencies.jar

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


* Lecture du state
```bash
#pulsar-admin functions querystate --fqfn talk/demo/orderfilter -k average-amount -w
```

* Lecture du topic avec les messages filtrés
```bash
pulsar-client consume -n 0 \
  -s "demo-fct-output-exclusive" \
  -t Exclusive \
  talk/demo/orders-us
```

* Lecture du topic avec les logs
```bash
pulsar-client consume -n 0 \
  -s "demo-fct-log-exclusive" \
  -t Exclusive \
  talk/demo/orders-logs
```


## Test 3 - pulsar io elasticsearch

* Lancement d'elastic + kibana

```bash
cd /Users/bruno/Documents/Perso/Dev/talk-intro-apache-pulsar
docker-compose up #start
```

* Deploiement du connector
```bash
# Create sink
cd /Users/bruno/Documents/Perso/Dev/talk-intro-apache-pulsar

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


###################################


```
pulsar-admin sink delete --tenant talk --namespace demo --name elasticsearch-sink
```
