<div align="center">
  
<h1 align="center">Transformación Digital y Soluciones Empresariales (TDSE)</h1>

</br>

<p align="center">
Collection of projects and exams from the "Transformación Digital y Soluciones Empresariales" course at Escuela Colombiana De Ingeniería Julio Garavito (ECI) 
</p>

</div>

</br>

## Purpose

The purpose of this repository is to centralize and document the practical work for the Digital Transformations and Enterprise Solutions course. It serves as a comprehensive portfolio of projects that translate key theoretical concepts into practical implementations.
The repository explores a wide range of crucial topics, including:

-   **Design and Architectural Patterns**: Applying foundational design patterns and high-level architectural styles to build robust applications.
-   **Custom Frameworks**: Developing frameworks from scratch to understand the fundamentals of inversion of control and request routing.
-   **Web Security**: Implementing user authentication, SSL/TLS encryption, and enterprise-level security best practices.
-   **Data Persistence**: Integration with databases using JPA/Hibernate and complete CRUD operations.
-   **Microservices Architecture**: Transitioning from monoliths to microservices with cloud service deployment (AWS Lambda, S3, API Gateway, Cognito).
-   **Containerization and Virtualization**: Using Docker and Docker Compose for consistent and portable deployments.
-   **Distributed Systems**: Implementing load balancers and request distribution using Round Robin algorithms.
-   **LLMs and RAG**: Exploring Large Language Models and Retrieval-Augmented Generation techniques using native APIs and LangChain.

Each project is a focused exercise designed to solidify the understanding of these key areas in contemporary software engineering.

</br>
</br>

## 💼 Projects Done

Below is a description of each project developed during the course.

#### distributed-apps

-   **Description**: Web server built from scratch in pure Java without external frameworks. Connects directly to the network to serve static files (HTML, CSS, images) and run a simple dynamic API. Supports multiple non-concurrent requests.
-   **Objective**: Understand how the fundamental conversation between a browser and server works, demystifying modern framework abstractions.

#### ioc-framework

-   **Description**: Lightweight custom framework for high-performance web development. Includes annotation-based routing (Spring-style), integrated static file server, request/response middleware, dependency injection, and integrated observability with custom tagging for metrics, logs, and traces.
-   **Objective**: Build a complete framework from scratch to understand principles of Inversion of Control (IoC), declarative routing, and request traceability in modern applications.

#### jpa-crud

-   **Description**: Comprehensive property management system built with Spring Boot. Provides complete CRUD operations for property inventory with modern web interface, advanced search, pagination, RESTful API, and data persistence with MySQL using JPA/Hibernate.
-   **Objective**: Learn to integrate relational databases into Spring Boot applications, implement complete CRUD operations, and handle large datasets efficiently.

#### microservices

-   **Description**: Twitter clone that allows users to create posts up to 140 characters and view them in a global stream. Evolves from a monolithic Spring Boot architecture to microservices deployed on AWS Lambda, with frontend on S3, JWT authentication with AWS Cognito, and communication through API Gateway.
-   **Objective**: Understand the transition from monoliths to microservices, implement serverless architectures on AWS, and manage distributed authentication with cloud services.

#### mini-framework

-   **Description**: Minimalist custom framework for rapid web service development. Features declarative API routing, integrated static file server, pipeline-based middleware system, and dependency injection for decoupled and testable code.
-   **Objective**: Explore the fundamentals of web frameworks by building one from scratch with focus on simplicity, performance, and modularity.

#### security

-   **Description**: Enterprise-level property management system with advanced security features. Implements user authentication with BCrypt hashing, SSL/TLS encryption via Apache reverse proxy with Let's Encrypt certificates, complete CRUD operations, secure web interface, and containerized deployment with Docker in a two-machine architecture.
-   **Objective**: Learn to implement enterprise-level security in web applications, including HTTPS communication encryption, secure password storage, and deployment with SSL termination.

#### virtualization

-   **Description**: IOC framework implementation with virtualization capabilities and containerized deployment. Includes annotation-based routing, integrated observability, and deployment using Docker to ensure portability and consistency across environments.
-   **Objective**: Understand principles of containerization and virtualization, implement reproducible deployments with Docker, and manage applications in virtualized environments.

#### llm-rag-workshop

-   **Description**: Collection of Jupyter notebooks with tutorials on Large Language Models (LLMs) and Retrieval-Augmented Generation (RAG). Includes examples of how to use LLMs natively with APIs like GPT, and a RAG system for information retrieval using LangChain in Python.
-   **Objective**: Explore the capabilities of large language models, learn to consume modern AI APIs, and implement retrieval-augmented systems to enhance LLM responses with specific contextual information.

</br>
</br>

## 📝 Exams

This section contains the material and solutions for the course examinations.

#### exam-1

-   **Description**: Implementation of the Facade pattern to access a backend API. The system consists of two servers: one acting as a facade providing an interface to the client, and another backend that processes requests via HttpConnection and returns results based on the requested URL.
-   **Objective**: Learn to implement the Facade architectural pattern to simplify interaction with complex systems and provide a unified interface to the client.

#### exam-2

-   **Description**: Distributed service to calculate the Collatz sequence. The architecture implements a math service deployed on two separate instances and a proxy service that distributes requests between both instances using a Round Robin scheme for load balancing.
-   **Objective**: Understand the principles of distributed systems, implement load balancers, and apply request distribution algorithms in multi-instance architectures.

</br>
</br>

## License

Distributed under the GPL-3.0 License. See `LICENSE` for more information.
