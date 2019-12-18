# Baixando imagem oficial do mongo
echo "Iniciando deploy..."
echo ''
echo "*** MONGODB - INICIO ***"
echo "Baixando imagem oficial do Mongo..."
docker pull mongo

# Remover instancias existentes...
#docker rm -f "$(docker ps -qa -f name='mongo_itau')" > /dev/null
#docker rm -f "$(docker ps -qa -f name='consulta_consignado_app')" > /dev/null

# Criando rede virtual
docker network create itau

# Para quebrar a linha antes de informar mensagem de criacao do container
echo "Criando container...";

# Iniciando container e expondo a porta
docker run --name mongo_itau -d -p 27017:27017 --network=itau mongo > /dev/null

# Informa ID do container criado
echo "Container criado, ID: $(docker ps -q -f name=mongo_itau)"
echo "*** MONGODB - FIM ***"
echo ''
echo "*** JAVA - INICIO ***"
echo "Compilando aplicação..."
mvn clean package

echo "Criando imagem da aplicação..."
# Gerendo imagem da aplicação baseado no dockerfile presente no diretorio atual
docker build -t consulta_consignado:1.0 -f Dockerfile .

# Iniciando container
echo "Executando a aplicação..."
docker run --name consulta_consignado_app -d -p 8080:8080 --network=itau consulta_consignado:1.0 > /dev/null

echo "*** JAVA - FIM ***"

read -p "Finalizado, pressione qualquer tecla para continuar..."
