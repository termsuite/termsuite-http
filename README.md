`termsuite-http` is an HTTP API for [TermSuite](http://termsuite.github.io/).

Currently, only preprocessing is supported. Terminology extraction and bilingual alignment ar enot supported.

## Getting started

### From sources

Prerequisites:

 * Gradle
 * Git
 * TreeTagger installed locally for TermSuite. See [instructions](http://termsuite.github.io/documentation/pos-tagger-lemmatizer/).


Clone the repo:

```
$ git clone git@github.com:termsuite/termsuite-http.git
```

Build the sources:

```
$ cd termsuite-http
$ gradle clean jar
```

Configure termsuite-http:

```
$ cp termsuite-http.properties.sample termsuite-http.properties
$ vi termsuite-http.properties
```

Edit the `termsuite-http.properties` according to your needs and to your local TreeTagger install.

Finally, start termsuite-http:

```
$ java -Xmx512m -Dlogback.configurationFile=logback.xml \
    -cp build/libs/termsuite-http-1.0.0.jar \
    fr.univnantes.termsuite.http.TermSuiteHttp
```

### With docker

See [TermSuite HTTP Docker container](https://github.com/termsuite/termsuite-http-docker)

## Using the API

### `POST /preprocess`

Applies TermSuite preprocessings on an input text file that is passed as request body, and returns the preprocessed annotations in `JSON` as response body:

```
curl http://0.0.0.0:4567/preprocess \
    --data-binary @myfile.txt \
    -H "Content-Type: text/plain \
    > myfile-preprocessed.json
```

### `GET /preprocess`

Applies TermSuite preprocessings on an input text passed as parameter, and returns the preprocessed annotations in `JSON` as response body:

```
curl -G http://0.0.0.0:4567/preprocess?data \
  --data-urlencode "text=The black cat is eating the mouse."
```

## Configuration

Available config parameters in `termsuite-http.properties` are:

 * **tagger.path**: the path to local TreeTagger,
 * **termsuite.lang**: the language of TermSuite preprocessor, (default: `en`)
 * **termsuite.preprocessor.max_requests**: the max number of preprocessing requests before inner preprocessor service is restarted,
 * **http.port**: customizes the listening port.

**Example**

```
termsuite.lang=fr
termsuite.preprocessor.max_requests=100
tagger.path=/opt/treetagger
http.port=4567
```
