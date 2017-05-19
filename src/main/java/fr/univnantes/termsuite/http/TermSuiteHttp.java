package fr.univnantes.termsuite.http;

import spark.Spark;

public class TermSuiteHttp {
	
	public static void main(String[] args) {
		// Ensures properties can be load without any pb
		TermSuiteHttpConfig.properties();
		
		routes();
	}

	private static void routes() {
		
		
		Spark.post("/preprocess", "text/plain", PreprocessApi::preprocessPostFromJson);
		Spark.get("/preprocess", PreprocessApi::getPreprocess);
		
		Spark.exception(MissingParameterException.class, (exception, request, response) -> {
			response.status(400);
			response.body(exception.getMessage());
		});
	}
	
}
