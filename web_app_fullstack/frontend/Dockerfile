FROM node:18-alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN npm ci 
COPY . .
RUN npm run build --prod 

FROM nginx:alpine
COPY --from=builder /app/dist/ft /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf  
EXPOSE 80