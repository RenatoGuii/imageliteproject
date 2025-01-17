FROM node:18-alpine

WORKDIR /app

# Copia as dependências do projeto para dentro do container
COPY ./package*.json .

# Instala todas as dependências, incluindo devDependencies, para garantir o build
RUN npm install

# Copia o restante do projeto para dentro do container
COPY . .

# Cria o build do projeto
RUN npm run build

# Remove as devDependencies para otimizar o ambiente de produção
RUN npm prune --production

# Expõe a porta de acesso à aplicação
EXPOSE 3000

# Usa o usuário "node" em vez de "root" para maior segurança
USER node

# Comando de inicialização do container
ENTRYPOINT [ "npm", "start" ]
