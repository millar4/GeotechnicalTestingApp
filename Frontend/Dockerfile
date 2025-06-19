# Stage 0: Define a base image and install serve
FROM node:18-alpine AS base
RUN npm install -g serve --unsafe-perm

# Phase 1: build phase
FROM base AS builder
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Phase 2: Production phase
FROM base
WORKDIR /app
COPY --from=builder /app/build ./build
EXPOSE 3000
CMD ["serve", "-s", "build", "-l", "3000"]
