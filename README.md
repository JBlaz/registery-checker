# My Clojure App

## Overview
This is a simple Clojure application that serves as a template for building executable JAR files. It includes a main namespace with an entry point function and is structured to allow for easy expansion and modification.

## Project Structure
```
my-clojure-app
├── src
│    └── core.clj
├── resources
├── project.clj
└── README.md
```

## Getting Started

### Prerequisites
Make sure you have the following installed:
- [Leiningen](https://leiningen.org/) - for managing Clojure projects.

### Building the Project
To build the project and create an executable JAR, run the following command in the project root directory:
```
lein uberjar
```

### Running the Application
Once the JAR is built, you can run the application using:
```
java -jar target/my-clojure-app-0.1.0-SNAPSHOT-standalone.jar
```

### Usage
You can modify the `-main` function in `src/my_clojure_app/core.clj` to implement your application's logic and handle command-line arguments as needed.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.