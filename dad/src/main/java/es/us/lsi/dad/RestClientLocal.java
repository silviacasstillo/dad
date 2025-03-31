package es.us.lsi.dad;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

public class RestClientLocal extends AbstractVerticle {

	// Web client util instance
	private RestClientUtil restClientUtil;

	public void start(Promise<Void> startFuture) {
		WebClientOptions options = new WebClientOptions().setUserAgent("RestClientApp/2.0.2.1");
		
		// Optimization for servlet-based connection (state-less)
		options.setKeepAlive(false);
		restClientUtil = new RestClientUtil(WebClient.create(vertx, options));

		/*
		 * Get resource list operation
		 */
		Promise<Sensores[]> resList = Promise.promise();
		resList.future().onComplete(complete -> {
			System.out.println("-----------------------------------------------------------");
			if (complete.succeeded()) {
				System.out.println("Resource list obtained");
				if (complete.result() != null) {
					System.out.println(complete.result().toString());
				} else {
					System.out.println("Empty body");
				}
			} else {
				System.out.println("Resource list not obtained");
				System.out.println(complete.cause().toString());
			}
		}).onSuccess(success -> {
			// System.out.println(success.toString());
		}).onFailure(failure -> {
			// System.out.println(failure.toString());
		});
		
		restClientUtil.getRequest(8080, "http://localhost", "api/users", Sensores[].class, resList);

		/*
		 * Post resource operation
		 */
		/*Promise<Sensores> resPost = Promise.promise();
		resPost.future().onComplete(complete -> {
			System.out.println("-----------------------------------------------------------");
			if (complete.succeeded()) {
				System.out.println("Resource added");
				if (complete.result() != null) {
					System.out.println(complete.result().toString());
				} else {
					System.out.println("Empty body");
				}
			} else {
				System.out.println("Resource not added");
				System.out.println(complete.cause().toString());
			}
		});

		restClientUtil.postRequest(8080, "http://localhost", "api/users",
				new Sensores(3, "Nuevo", "Usuario", Calendar.getInstance().getTimeInMillis(), "nuevo_usuario", "pass"),
				Sensores.class, resPost);*/

		/*
		 * Get single resource operation
		 */
		Promise<Sensores> res = Promise.promise();
		res.future().onComplete(complete -> {
			System.out.println("-----------------------------------------------------------");
			if (complete.succeeded()) {
				System.out.println("Resource obtained");
				if (complete.result() != null) {
					System.out.println(complete.result().toString());
				} else {
					System.out.println("Empty body");
				}
			} else {
				System.out.println("Resource not obtained");
				System.out.println(complete.cause().toString());
			}
		});

		restClientUtil.getRequest(8080, "http://localhost", "api/users/3", Sensores.class, res);

		/*
		 * Put resource operation
		 */
		/*Promise<Sensores> resPut = Promise.promise();
		resPut.future().onComplete(complete -> {
			System.out.println("-----------------------------------------------------------");
			if (complete.succeeded()) {
				System.out.println("Resource modified");
				if (complete.result() != null) {
					System.out.println(complete.result().toString());
				} else {
					System.out.println("Empty body");
				}
			} else {
				System.out.println("Resource not added");
				System.out.println(complete.cause().toString());
			}
		});

		restClientUtil.putRequest(8080, "http://localhost", "api/users/3", new Sensores(3, "Nuevo", "Usuario_modificado",
				Calendar.getInstance().getTimeInMillis(), "nuevo_usuario", "pass"), Sensores.class, resPut);

		/*
		 * Get resource operation using params
		 */
		Promise<Sensores[]> resWithParams = Promise.promise();
		resWithParams.future().onComplete(complete -> {
			System.out.println("-----------------------------------------------------------");
			if (complete.succeeded()) {
				System.out.println("Resource with params obtained");
				if (complete.result() != null) {
					System.out.println(complete.result().toString());
				} else {
					System.out.println("Empty body");
				}
			} else {
				System.out.println("Resource with params not obtained");
				System.out.println(complete.cause().toString());
			}
		});
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", "Nuevo");
		restClientUtil.getRequestWithParams(8080, "http://localhost", "api/Sens", Sensores[].class, resWithParams, params);

		/*
		 * Delete resource operation
		 */
		Promise<String> resDelete = Promise.promise();
		resDelete.future().onComplete(complete -> {
			System.out.println("-----------------------------------------------------------");
			if (complete.succeeded()) {
				System.out.println("Resource deleted");
				if (complete.result() != null) {
					System.out.println(complete.result().toString());
				} else {
					System.out.println("Empty body");
				}
			} else {
				System.out.println("Resource not deleted");
				System.out.println(complete.cause().toString());
			}
		});

		restClientUtil.deleteRequest(8080, "http://localhost", "api/users/3", resDelete);

		/*
		 * Combining several promises
		 */
	/*	Promise<Sensores> resPost2 = Promise.promise();
		Promise<Sensores> resPost3 = Promise.promise();
		Promise<Sensores> resPost4 = Promise.promise();
		restClientUtil.postRequest(8080, "http://localhost", "api/users",
				new Sensores(3, "Nuevo3", "Usuario3", Calendar.getInstance().getTimeInMillis(), "nuevo_usuario3", "pass"),
				Sensores.class, resPost2);
		restClientUtil.postRequest(8080, "http://localhost", "api/users",
				new Sensores(4, "Nuevo4", "Usuario4", Calendar.getInstance().getTimeInMillis(), "nuevo_usuario4", "pass"),
				Sensores.class, resPost3);
		restClientUtil.postRequest(8080, "http://localhost", "api/users",
				new Sensores(5, "Nuevo5", "Usuario5", Calendar.getInstance().getTimeInMillis(), "nuevo_usuario5", "pass"),
				Sensores.class, resPost4);

		CompositeFuture.all(resPost2.future(), resPost3.future(), resPost4.future())
				.onComplete(new Handler<AsyncResult<CompositeFuture>>() {

					@Override
					public void handle(AsyncResult<CompositeFuture> event) {
						System.out.println("-----------------------------------------------------------");
						System.out.println("Composite futures");
						System.out.println(resPost2.future().result());
						System.out.println(resPost3.future().result());
						System.out.println(resPost4.future().result());

					}
				});*/

	}

}
