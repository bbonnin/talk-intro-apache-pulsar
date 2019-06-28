# Demo 

## Premières etapes

> Montrer les outils en ligne de commande

* pulsar
* pulsar-admin
* pulsar-client

## Run

* Démarrage en mode standalone
```bash
$ pulsar standalone

$ pulsar-admin clusters list
$ pulsar-admin brokers list standalone
$ pulsar-admin brokers healthcheck
$ pulsar-admin brokers get-internal-config
```

* Création tenant et namespace

```bash
$ pulsar-admin tenants create talk
$ pulsar-admin namespaces create talk/demo
```

## Test 1 (basic producer avec schema)

* Lancement de OrderGenerator

* Consommation des messages
```bash
$ pulsar-client consume -n 0 \
  -s "demo-fct-output-exclusive" \
  -t Exclusive \
  talk/demo/orders-all
```

* Voir le schema utilisé
```bash
$ pulsar-admin schemas get talk/demo/orders-all | jq -r '.schema' | base64 -D | jq
```


## Test 2 - fonction de filtrage

* Lancement de la fonction en mode local
```bash
$ pulsar-admin functions localrun \
   --jar $(pwd)/target/pulsar-examples-jar-with-dependencies.jar \
   --className io.millesabords.pulsar.examples.ecommerce.FilterByCountryFunction \
   --fqfn talk/demo/orderfilter \
   --inputs persistent://talk/demo/orders-all \
   --output persistent://talk/demo/orders-but-us \
   --user-config '{dbpath: "'$(pwd)'/GeoLite2-Country.mmdb"}' \
   --state-storage-service-url bk://localhost:4181
```

* Lecture du state
```bash
$ pulsar-admin functions querystate --fqfn talk/demo/orderfilter -k average-amount -w
```

* Lecture du topic avec les messages filtrés
```bash
pulsar-client consume -n 0 \
  -s "demo-fct-output-exclusive" \
  -t Exclusive \
  talk/demo/orders-us
```


## Test 3 - pulsar io elasticsearch

* Lancement d'elastic + kibana

```bash
$ docker-compose start #up
```

* Deploiement du connector
```bash
# Create sink
$ pulsar-admin sink create \
      --tenant talk \
      --namespace demo \
      --name elasticsearch-sink \
      --sink-type elasticsearch \
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


* démarrer pulsar-express



Cas d'utilisation:

- Archi :
  OK pulsar standalone
  OK 1 producer (illustration du dev de producteur avec schema)
  KO 1 consommateur (traitement des messages) => MONTER PLUTOT LE CLIENT EN MODE CLI
  OK 1 fonction qui filtre (par pays) + FABRICATION DE STATS (APPELE LA FONCTION : ????)
  KO 1 fonction qui fait des stats (moyenne) -> CF CI-DESSUS
  KO 1 fonction pour des alertes : C'EST TROP
  - 1 IO (elasticsearch pour avoir dashboard kibana) => peut faire des diagrammes sur la moyennes des paniers, cela peut éviter de faire une fonction de stats ? ou ne prendre que la partie geo-location (suite à enrichissement par la fonction)

- site ecommerce
  - enrichissement des transactions (geoloc de l'adresse ip, anonymisation du numeros de CB)
  - comptage des paniers moyens
  - autres stats sur le panier
  - filtrage pour etude sur un produit particulier ou une origine de l'acheteur
  - detection de fraude (comment ? plusieurs achats d'un meme user dans des pays differents mais à des dates tres proches ?)
- capteurs
  - alertes sur seuil
  - alertes configurées par user config