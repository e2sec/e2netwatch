FROM node:8.11.3-alpine AS build

WORKDIR /app

COPY package.json /app/

RUN npm install

COPY ./ /app/

RUN npm run build:prod

FROM nginx:1.15-alpine

COPY --from=build /app/dist/e2Netwatch/ /usr/share/nginx/html

COPY  ./nginx/default.conf /etc/nginx/conf.d/default.conf