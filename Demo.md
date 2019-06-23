Demo 

* montrer les outils en ligne de commande
  * pulsar
  * pulsar-admin
  * pulsar-client

* démarrage de Pulsar en mode standalone
  * CLI
  ```
  $ pulsar standalone

  $ pulsar-admin clusters list

  $ pulsar-admin brokers list standalone

  $ pulsar-admin brokers healthcheck

  $ pulsar-admin brokers get-internal-config
  ```



* démarrer pulsar-express



Cas d'utilisation:

- Archi :
  - pulsar standalone
  - 1 producer (illustration du dev de producteur avec schema)
  - 1 consommateur (traitement des messages)
  - 1 fonction qui filtre (par pays ?)
  - 1 fonction qui fait des stats (moyenne)
  - 1 fonction pour des alertes ?
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