## TDSE-rag

Este repositorio contiene un cuaderno de Jupyter (`tutorial.ipynb`) que implementa un ejemplo práctico de RAG
(Retrieval-Augmented Generation) utilizando LangChain y Pinecone. El objetivo del cuaderno es reproducir y
aprender el flujo del tutorial oficial de LangChain para RAG y usar Pinecone como almacén vectorial para las
incrustaciones (embeddings).

### Contenido y propósito

- `tutorial.ipynb`: guía paso a paso en español que sigue el tutorial de LangChain sobre RAG ([Guía de LangChain para RAG](https://docs.langchain.com/oss/python/langchain/rag)).

El cuaderno incluye las siguientes secciones:

- Configuración e instalación de dependencias.
- Selección y configuración del modelo de chat y del modelo de incrustaciones (OpenAI).
- Conexión e inicialización de Pinecone como base de datos vectorial.
- Carga de documentos (ejemplo: artículo de Lilian Weng), división en fragmentos y creación del índice.
- Ejemplos de recuperación y generación: agente RAG, cadenas con contexto inyectado y búsquedas iterativas.

### Referencias principales

- Tutorial de LangChain (RAG): [https://docs.langchain.com/oss/python/langchain/rag](https://docs.langchain.com/oss/python/langchain/rag)
- Documentación de Pinecone para integración y uso como vector database.

### Requisitos y variables de entorno

El cuaderno está pensado para ejecutarse localmente y únicamente requiere que proporciones las claves de API
mediante variables de entorno. No se deben subir estas claves al repositorio.

Variables esperadas (ejemplo en un archivo `.env`):

- `OPENAI_API_KEY` — clave de la API de OpenAI usada para el modelo de chat y para generar embeddings.

- `PINECONE_API_KEY` — clave de la API de Pinecone para acceder al servicio de vectores.

Puedes crear un archivo `.env` en la raíz con estas dos variables. El cuaderno intenta leerlas
desde el entorno y, si no están, pedirá su ingreso interactivamente.

### Dependencias

El cuaderno incluye celdas para instalar las dependencias necesarias. Algunas de las principales son:

- langchain
- langchain-text-splitters
- langchain-community
- langchain-openai
- langchain-pinecone
- bs4
- pinecone

### Ejecución rápida

1. Crear un entorno virtual (recomendado) y abrir el proyecto.
2. Añadir `OPENAI_API_KEY` y `PINECONE_API_KEY` a tu `.env` o exportarlas en el entorno.
3. Abrir `tutorial.ipynb` y ejecutar las celdas en orden. El cuaderno contiene las instrucciones de instalación de
paquetes y las celdas de configuración para inicializar los clientes de OpenAI y Pinecone.
