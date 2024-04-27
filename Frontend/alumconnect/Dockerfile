# Use Node.js as the base image
FROM node:latest AS build

# Set the working directory in the container
WORKDIR /app

# Copy package.json and package-lock.json to install dependencies
COPY package.json ./

# Install dependencies
RUN npm install

# Copy the application code
COPY . .

# Build the React + Vite application
# RUN npm run build

# Use Nginx to serve the built files
# FROM nginx:alpine AS final

# Copy the built files from the build stage to the Nginx directory
# COPY --from=build /app/dist /usr/share/nginx/html

# Expose port 80 to allow external access
# EXPOSE 80
EXPOSE 8080

# Start Nginx server
# CMD ["nginx", "-g", "daemon off;"]
CMD ["npm", "run", "dev"]
