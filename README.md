# Spring boot ELK(Elasticsearch, Logstash, and Kibana) Example
* Le but de ce projet est d'utiliser le ELK stack avec Spring Boot pour analyser les logs générés.

## ELK (Elasticsearch, Logstash, et Kibana): 
* Elasticsearch est une base de données NoSQL, un moteur de recherche et d'analyse distribué et en open source pour tout type de données, y compris les données textuelles, numériques, géospatiales, structurées et non structurées
* Logstash est un outil de pipeline de logs qui accepte les entrées de diverses sources, exécute différentes transformations et exporte les données vers diverses cibles. Il s'agit d'un pipeline de collecte de données dynamique avec un écosystème de plugins extensible.
* Kibana est un outil open source de visualisation de données pour elasticsearch

## STEPS
### Elasticseach
1.  Télécharger la dernière version d'elasticsearch [Elasticsearch downloads](https://www.elastic.co/downloads/elasticsearch)
2.  Run elasticsearch.bat dans le cmd (sous le dossier /bin d'elasticsearch). (Par défaut Elasticsearch est lancé sur localhost:9200)

### Kibana
1. Télécharger la dernière version de Kibana [Kibana downloads](https://www.elastic.co/downloads/kibana)
2. Modifier kibana.yml (sous le dossier config) pour pointer sur elasticsearch
    ```
        server.port: 5601
        elasticsearch.hosts: ["http://localhost:9200"]
    ```
3.  Run kibana.bat dans le cmd (sous le dossier /bin de kibana). (Par défaut Kibana est lancé sur localhost:5601)

### Spring boot
One arrive maintenant à la partie Spring boot, on va créer un projet spring boot qui génère des logs, on va utiliser log4j pour le logging
1. A l'aide de spring initializr, créer un nouveau projet avec Spring Web starter
2. Ajouter les dépendances pour log4j:

    ```xml
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-log4j2</artifactId>
        </dependency>
        <!-- exclude logback , add log4j2 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-logging</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>
    ```
3. Définir l'emplacement du log file dans applicaiton.properties
    ```
    logging.file.name= D:/Wassim/wkprojects/elk-spring-boot/log-files/spring-boot-elk.log
    ```
4. Définir un contoleur qui expose une API reste. Les appels à ces API écriront dans le fichier log qu'on vient de définir
    ```java
    @RestController
    class ELKController {
    
    	private static final Logger logger = LogManager.getLogger(ELKController.class);

    	    @GetMapping("/exception")
            public ResponseEntity<String> throwException() {
                String reponse;
                try{
                    throw new Exception("Une exception est levée ..");
                } catch (Exception e){
                    logger.error("Une exception est levée ..");
                    e.printStackTrace();
                    reponse = e.getMessage();
                }
                return new ResponseEntity<>(reponse, HttpStatus.OK);
            }
    }
    ```

### Logstash
1. Télécharger la dernière version de logstash [Logstash downloads](https://www.elastic.co/downloads/logstash)
2. Configurer le pipeline de logstash:  
![Image](https://github.com/Wassimkal-projects/elk-spring-boot/blob/master/src/main/resources/readmeimages/logstash-pipeline.jpg) 
    * Créer un fichier de configuration nommé logstash.conf (sous le dossier /config par exemple)
   
    ```
    input {
      file {
        type => "java"
        path => "D:/Wassim/wkprojects/elk-spring-boot/log-files/spring-boot-elk.log"
        codec => multiline {
          pattern => "^%{YEAR}-%{MONTHNUM}-%{MONTHDAY} %{TIME}.*"
          negate => "true"
          what => "previous"
        }
      }
    }
     
    filter {
      #If log line contains tab character followed by 'at' then we will tag that entry as stacktrace
      if [message] =~ "\tat" {
        grok {
          match => ["message", "^(\tat)"]
          add_tag => ["stacktrace"]
        }
      }
     
    }
     
    output {
       
      stdout {
        codec => rubydebug
      }
     
      # Sending properly parsed log events to elasticsearch
      elasticsearch {
        hosts => ["localhost:9200"]
      }
    }
    ``` 
    **NB:** *Change the input path to your log file path* 
3. Run logstash: logstash -f ../config/logstash.conf dans le cmd (sous /bin)

### Create index pattern in Kibana
1. Run spring boot app
2. Go to localhost:8080/exception to generate exception
3. Go to kibana UI console ( http://localhost:5601 )
4. Click on "Connect to your Elasticsearch index"
5. In index pattern type **logstash-** 
