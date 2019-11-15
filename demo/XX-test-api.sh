#!/bin/bash

# Docs: https://pulsar.apache.org/docs/latest/reference/RestApi/

curl_common_opts=-v
pulsar_url=http://localhost:8080

echo "== Liste des clusters =="
curl $curl_common_opts $pulsar_url/admin/v2/clusters


echo "== cluster =="
curl $curl_common_opts $pulsar_url/admin/v2/clusters/standalone

echo "== namespaceIsolationPolicies =="
curl $curl_common_opts $pulsar_url/admin/v2/clusters/standalone/namespaceIsolationPolicies

echo "== Liste des tenants =="
curl $curl_common_opts $pulsar_url/admin/v2/tenants

echo "== Liste des namespaces =="
curl $curl_common_opts $pulsar_url/admin/v2/namespaces/demo

echo "== dispatchRate pour 1 namespace =="
curl $curl_common_opts $pulsar_url/admin/v2/namespaces/demo/devoxx/dispatchRate

echo "== messageTTL pour 1 namespace =="
curl $curl_common_opts $pulsar_url/admin/v2/namespaces/demo/devoxx/messageTTL

echo "== persistence pour 1 namespace =="
curl $curl_common_opts $pulsar_url/admin/v2/namespaces/demo/devoxx/persistence
