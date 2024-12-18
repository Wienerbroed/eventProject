# Use the official Node.js base image
FROM node:18

# Set the working directory inside the container
WORKDIR /app

# Copy package.json and package-lock.json from frontend
COPY frontend/package*.json ./

# Install dependencies
RUN npm install

# Copy the JavaScript backend source code
COPY frontend/src ./src

# Expose the port your app listens on
EXPOSE 3000

# Run the JavaScript app
CMD ["node", "src/app.js"]
