# Get Started 👋

## Enviroment Configuration

1. First install on your Vs Code IDE, the Spring Boot Extension Pack that includes: 

    * Spring Boot Tools
    * Spring Initializr
    * Spring Boot Dashboard

2. Make sure you have the JDK, extension pack for Java already install 🔥

3. This software version need at least java version 8

## Are you creating the project?

The Spring Initializr extension allows you to search for dependencies and generate new Spring Boot projects.

Here is a list of facts using ```vscode-spring-initializr```:

* Once you have the extension installed, open the Command Palette (⇧⌘P) and type Spring Initializr to start generating a Maven or Gradle project and then follow the wizard.

    - Service URL
    - Project Name - Artifact -
    - Type: Maven
    - Packaging: jar
    - Java Version: 8
    - Language: Java
    - Group: net.state
    - Package: net.state.statemachineserver

* Add new dependecies in your pom.xml file just right clicking and pushing your specific libraries -Add straters- 

## Clone the project
* ssh
``` sh
git clone git@github.com:juanosorio0219/state-machine-client.git
```
* https
```sh
git clone https://github.com/juanosorio0219/state-machine-client.git
```

## Folder structure

```bash
|- net.state.statemachineserver
    |- controller
        |- EntityController.java - Handle our entrypoint of our product -
    |- model
        |- Entity.java - Business Logic -
    |- repository
        |- EntityRepository - Communication wirh another data services layers -
```

## Deployment 📦

Create your **jar** executable maven project with the following line: 

```bash
./mvnw package
```

Using Azure Kubernetes Service cluster. 🐳

### What is Kubernetes? 🛳

Kubernetes is a cluster-based orchestrator. It provides your application with several benefits, such as availability, monitoring, scaling, and rolling updates.

* Cluster nodes
A cluster is node-based and there are two types of nodes:

**Control plane nodes:** These nodes host the cluster's control plane aspects and are reserved for services that control the cluster. They're responsible for providing the API you and all the other nodes use to communicate. No workloads are deployed or scheduled on these nodes.

**Nodes:** These nodes are responsible for executing custom workloads and applications, such as components from your cloud-based video rendering service.

### Cluster Creation 

* Create variables for the configuration values
```bash
export RESOURCE_GROUP=rg-state-machine
export CLUSTER_NAME=aks-state-machine
export LOCATION=eastus
```
* Create the resource group
```bash
az group create --name=$RESOURCE_GROUP --location=$LOCATION
```
Respose from the Azure CLI 🥭

```ts
{
  "id": "/subscriptions/d566fc78-dc22-4e5d-9917-e832b5354249/resourceGroups/rg-state-machine",
  "location": "eastus",
  "managedBy": null,
  "name": "rg-state-machine",
  "properties": {
    "provisioningState": "Succeeded"
  },
  "tags": null,
  "type": "Microsoft.Resources/resourceGroups"
}
```

* Create the AKS Cluster
```bash
az aks create \
    --resource-group $RESOURCE_GROUP \
    --name $CLUSTER_NAME \
    --node-count 2 \
    --enable-addons http_application_routing \
    --generate-ssh-keys \
    --node-vm-size Standard_B2s \
    --network-plugin azure
```
* Link your Kubernetes cluster with kubectl by running the following command in Cloud Shell.

```bash
az aks get-credentials --name $CLUSTER_NAME --resource-group $RESOURCE_GROUP
```

* Then if you just make use of your conneciton tool in this case kubectl running the following code line.

```bash
kubectl get nodes
```
- You will get

```bash
NAME                                STATUS   ROLES   AGE     VERSION
aks-nodepool1-14700414-vmss000000   Ready    agent   2m25s   v1.23.8
aks-nodepool1-14700414-vmss000001   Ready    agent   2m31s   v1.23.8
```

### App Deployment in AKS

1. Creates your ACR in your azure account, take in mind you need to create a local image of your product then login to your registry and push tour image like:

```bash
docker login registrystatemachines.azurecr.io
docker tag <imageID> registrystatemachines.azurecr.io/state-machines:v1
docker push registrystatemachines.azurecr.io/state-machines:v1
```

2. Using Azure CLI tool you need to az login <acr-name> --get-token and then

```bash
docker login registrystatemachines.azurecr.io -u 00000000-0000-0000-0000-000000000000 -p token-already-provided
```

3. Submit the deployment manifest to your cluster.

```bash
kubectl apply -f ./deployment.yaml
```

4. Run the kubectl get deploy command to check if the deployment was successful.

```bash
kubectl get deploy machines-website
```

5. Run the ```kubectl get pods``` command to check if the pod is running.

```bash
kubectl get pods
```

6. Enable network access to an application

    * Kubernetes servises types:

        - **ClusterIP:** This value exposes the applications internally only. This option allows the service to act as a port-forwarder and makes the service available within the cluster. This value is the default when omitted.

        - **NodePort:** This value exposes the service externally. It assigns each node a static port that responds to that service. When accessed through ```nodeIp:port```, the node automatically redirects the request to an internal service of the ClusterIP type. This service then forwards the request to the applications.

        - **LoadBalancer:** This value exposes the service externally by using Azure's load-balancing solution. When created, this resource spins up an Azure Load Balancer resource within your Azure subscription. Also, this type automatically creates a NodePort service to which the load balancer's traffic is redirected and a ClusterIP service to forward internally.

        - **ExternalName:** This value maps the service by using a DNS resolution through a CNAME record. You use this service type to direct traffic to services that exist outside the Kubernetes cluster.

7. Deploy the service and check it out

```bash
kubectl apply -f ./service.yaml
kubectl get service machines-website
```

8. Azure DNS zone list

```bash
az aks show \
  -g $RESOURCE_GROUP \
  -n $CLUSTER_NAME \
  -o tsv \
  --query addonProfiles.httpApplicationRouting.config.HTTPApplicationRoutingZoneName
```

9. Deploy the ingress and check it out

```bash
kubectl apply -f ./ingress.yaml
kubectl get ingress machines-website
```

10. Clean up resources 

https://portal.azure.com/

```bash
kubectl config delete-context aks-state-machine
```
