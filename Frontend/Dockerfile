# stage 1 - the build process
# base image
FROM node:10.14.2 AS build
# set working directory ( automaticly adds app directory if it doesn't exist )
WORKDIR /app
# copy everything into that directory
COPY ./ /app
# using yarn because npm installs packages sequentially. This slows down the performance significantly. Yarn solves this problem by installing these packages in parallel.
RUN npm install
RUN npm run build

# stage: 2 — the production environment
FROM nginx:alpine
COPY --from=build /app/build /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]