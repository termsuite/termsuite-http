package fr.univnantes.termsuite.http;

import java.io.IOException;
import java.io.StringWriter;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.uima.jcas.JCas;

import fr.univnantes.termsuite.api.TermSuite;
import fr.univnantes.termsuite.framework.service.PreprocessorService;
import fr.univnantes.termsuite.model.Document;
import fr.univnantes.termsuite.uima.readers.JsonCasSerializer;
import spark.Request;
import spark.Response;

public class PreprocessApi {
	private static PreprocessorService preprocessor = null;
	private static final AtomicInteger counter = new AtomicInteger(1);
	private static final Semaphore mutex = new Semaphore(1);
	

	public static String preprocessPostFromJson(Request request, Response response) {
		return prepareToJson(request.body());
	}

	public static String getPreprocess(Request request, Response response) {
		String text = request.queryParams("text");
		if(text == null)
			throw new MissingParameterException("text");
			
		return prepareToJson(text);
	}

	private static String prepareToJson(String text) {
		JCas cas = prepare(text);
		StringWriter writer = new StringWriter();
		try {
			JsonCasSerializer.serialize(writer, cas);
			return writer.toString();
		} catch (IOException e) {
			throw new RuntimeException(
					"JSON serialization failed: " + e.getMessage(), 
					e);
		}
	}
	
	private static JCas prepare(String txt) {
		mutex.acquireUninterruptibly();
		String url = "http://termsuite.github.io/" + counter.incrementAndGet();
		JCas cas = getPreprocessor().prepare(new Document(TermSuiteHttpConfig.getLang(), url), txt);
		mutex.release();
		return cas;
	}

	private synchronized static PreprocessorService getPreprocessor() {
		if(counter.get() % TermSuiteHttpConfig.getMaxRequests() == 0)
			preprocessor = null;
		
		if(preprocessor == null) {
			preprocessor = TermSuite.preprocessor()
					.setTaggerPath(TermSuiteHttpConfig.getTaggerPath())
					.asService(TermSuiteHttpConfig.getLang());
		}
		return preprocessor;
	}
}
