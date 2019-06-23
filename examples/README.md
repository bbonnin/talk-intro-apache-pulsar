# Pulsar

## Demo

* Create tenant/namespace
```bash
# Multi-tenancy
# - création entité
$ pulsar-admin tenants create talk

# - création namespace
$ pulsar-admin namespaces create talk/demo
```

* Deploy function
```bash
$ pulsar-admin functions create \
   --jar $(pwd)/target/pulsar-examples.jar --className io.millesabords.pulsar.examples.EnhancedHiFunction \
   --fqfn talk/demo/hello \
   --inputs persistent://talk/demo/input \
   --output persistent://talk/demo/output \
   --log-topic persistent://talk/demo/logs
   
# For the 'order' function
$ pulsar-admin functions localrun \
   --jar $(pwd)/target/pulsar-examples-jar-with-dependencies.jar --className io.millesabords.pulsar.examples.ecommerce.FilterByCountryFunction \
   --fqfn talk/demo/orderfilter \
   --inputs persistent://talk/demo/orders-all \
   --output persistent://talk/demo/orders-but-us \
   --user-config '{dbpath: "'$(pwd)'/GeoLite2-Country.mmdb"}' \
   --state-storage-service-url bk://localhost:4181

$ pulsar-admin functions querystate --fqfn talk/demo/orderfilter -k average-amount -w
```

* List functions
```bash
pulsar-admin functions list --tenant talk --namespace demo
```

* Delete a function
```bash
pulsar-admin functions delete --fqfn talk/demo/hello
```

* Get logs
```bash
pulsar-client consume \
    -n 0 -r 1 \
    -s "demo-fct-logs-exclusive" \
    -t Exclusive \
    talk/demo/logs
```

```bash
pulsar-client consume \
    -n 0 -r 1 \
    -s "demo-fct-output-exclusive" \
    -t Exclusive \
    talk/demo/output
```

* Produce messages
```bash
pulsar-client produce \
    -m "hello world" \
    -n 100 -r 1 \
    talk/demo/input
```

## Admin

```bash
$ pulsar-admin clusters list
standalone

$ pulsar-admin clusters get standalone
{
  "serviceUrl" : "http://mbp-de-bruno:8080",
  "brokerServiceUrl" : "pulsar://mbp-de-bruno:6650"
}

$ pulsar-admin tenants list
public
sample

$ pulsar-admin tenants get public
{
  "adminRoles" : [ ],
  "allowedClusters" : [ "standalone" ]
}

$ pulsar-admin namespaces list public
public/default
public/functions

$ pulsar-admin persistent list public/default       
persistent://public/default/demo-topic
persistent://public/default/my-topic
```

## SQL

```bash
# In case, you use a strange JVM (for example, AdoptOpneJDK)
export JAVA_TOOL_OPTIONS="-Djava.vendor='Oracle Corporation'"
pulsar sql-worker run
```