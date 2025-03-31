package es.us.lsi.dad;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class RestServer extends AbstractVerticle {

	private Map<Integer, Sensores> sensores = new HashMap<Integer, Sensores>();
	private Gson gson;

	public void start(Promise<Void> startFuture) {
		// Creating some synthetic data
		createSomeData(25);

		// Instantiating a Gson serialize object using specific date format
		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

		// Se crea un Router para manejar las rutas de la API REST.
		Router router = Router.router(vertx);

		//Inicializacion del servidor
		// Handling any server startup result
		vertx.createHttpServer().requestHandler(router::handle).listen(8080, result -> {
			if (result.succeeded()) {
				startFuture.complete();
			} else {
				startFuture.fail(result.cause());
			}
		});

		// Defining URI paths for each method in RESTful interface, including body
		// handling by /api/sensores* or /api/sensores/*
		//
		router.route("/api/sensores*").handler(BodyHandler.create()); //Permite manejar cuerpos de solicitud (POST, PUT) para que puedan ser leídos.
		router.get("/api/sensores").handler(this::getAllWithParams);
		router.get("/api/sensores/:id_sensor").handler(this::getOne);
		router.post("/api/sensores").handler(this::addOne);
		router.delete("/api/sensores/:id_sensor").handler(this::deleteOne);
		router.put("/api/sensores/:id_sensor").handler(this::putOne);
	}

	@SuppressWarnings("unused")
	private void getAll(RoutingContext routingContext) { // representa solicitud y resouesta
		routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
				.end(gson.toJson(sensores.values()));
	}

	private void getAllWithParams(RoutingContext routingContext) {
		//como son parametros opcionales se lo debemos pasar por queryParams 
		final String unit = routingContext.queryParams().contains("unit") ? 
				routingContext.queryParam("unit").get(0) : null;
		final String id_device = routingContext.queryParams().contains("id_device") ? 
				routingContext.queryParam("id_device").get(0) : null;
		final String status = routingContext.queryParams().contains("status") ? 
				routingContext.queryParam("status").get(0) : null;
		
		routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
				.end(gson.toJson(sensores.values().stream().filter(elem -> {
					boolean res = true;
					res = res && unit != null ? elem.getUnit().equals(unit) : true;
					res = res && id_device != null ? elem.getId_device().equals(id_device) : true;
					res = res && status != null ? elem.getStatus().equals(status) : true;
					return res;
				}).collect(Collectors.toList())));
	}

	private void getOne(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("id_sensor")); //nos lo pasa por url de la ruta
		// el parametro debe escribirse igual q arriba
		if (sensores.containsKey(id)) {
			Sensores ds = sensores.get(id);
			routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
					.end(gson.toJson(ds));
		} else {
			routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(204)
					.end();
		}
	}

	private void addOne(RoutingContext routingContext) {
		final Sensores sensor = gson.fromJson(routingContext.getBodyAsString(), Sensores.class);
		sensores.put(sensor.getId_sensor(), sensor);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(gson.toJson(sensor));
	}

	private void deleteOne(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("id_sensor"));
		if (sensores.containsKey(id)) {
			Sensores sensor = sensores.get(id);
			sensores.remove(id);
			routingContext.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
					.end(gson.toJson(sensor));
		} else {
			routingContext.response().setStatusCode(204).putHeader("content-type", "application/json; charset=utf-8")
					.end();
		}
	}

	private void putOne(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("sensorid"));
		Sensores ds = sensores.get(id);
		final Sensores element = gson.fromJson(routingContext.getBodyAsString(), Sensores.class);
		
		//actualizamos solo la propiedad q le indicamos
		ds.setId_device(element.getId_device());
		ds.setUnit(element.getUnit());
		ds.setStatus(element.getStatus());
		//actualizamos todos los valores
		sensores.put(ds.getId_sensor(), ds);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(gson.toJson(element));
	}

	private void createSomeData(int number) {
		Random rnd = new Random();
		IntStream.range(0, number).forEach(elem -> {
			int id = rnd.nextInt();
			sensores.put(id, new Sensores(id, id, id,"status_" + id));
		});
	}

}
