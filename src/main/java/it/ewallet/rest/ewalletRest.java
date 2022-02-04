package it.ewallet.rest;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import it.entity.*;

@Path("/ewallet")
public class ewalletRest {
	
	//Attributi
	private static ArrayList<ContoCorrente> conti = new ArrayList<>();
	private static ArrayList<Movimento> movimenti = new ArrayList<>();
	private static int idMovimentoSerial =0;
	
	//METODI
	//GET
															//valore_iban
	// http://localhost:8080/ProgettoSettimana8/rest/ewallet/shafuasif
	@GET
	@Path("/{ibanconto}")
	@Produces(MediaType.APPLICATION_JSON)		//Produces e' quando ritorno qualcosa
	public ContoCorrente getContoCorrente(@PathParam("ibanconto") String iban) {
		for(ContoCorrente c : conti) {
			if(c.getIban().equals(iban)) {
				return c;
			}
		}
		return null;
	}
	
																			//valore_iban
	// http://localhost:8080/ProgettoSettimana8/rest/ewallet/movimento/lista/aaa
	@GET
	@Path("/movimento/lista/{iban}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Movimento> getMovimento(@PathParam("iban") String iban) {	//stampa tutti i movimenti con quell'iban
		ArrayList<Movimento> lista = new ArrayList<>();
		for(Movimento m : movimenti) {
			if(m.getIbanConto().equals(iban)) {
				lista.add(m);
			}
		}
		return lista;
	}
	
	// http://localhost:8080/ProgettoSettimana8/rest/ewallet/movimento
	@GET
	@Path("/movimento/{idmovimento}")
	@Produces(MediaType.APPLICATION_JSON)
	public Movimento getMovimento(@PathParam("idmovimento") int id) {
		for(Movimento m : movimenti) {
			if(m.getId()==id) {
				return m;
			}
		}
		return null;
	}
	
	// http://localhost:8080/ProgettoSettimana8/rest/ewallet
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<ContoCorrente> getAllConti(){
		return conti;
	}
	
	// http://localhost:8080/ProgettoSettimana8/rest/ewallet/movimento
	@GET
	@Path("/movimento")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Movimento> getAllMovimenti(){
		return movimenti;
	}
	
	//Inserimenti
	// http://localhost:8080/ProgettoSettimana8/rest/ewallet
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)	//e' per passare parametri
	public Response insertConto(ContoCorrente conto) {
		if(conto.getSaldo()<0) {	//controllo che il saldo non sia negativo
			return Response.status(406).entity("Il conto non puo' avere saldo negativo appena creato").build();
		}
		for(ContoCorrente c : conti) {
			if(c.getIban().equals(conto.getIban())) {
				return Response.status(406).entity("Conto corrente gia' presente").build();
			}
		}
		conti.add(conto);
		return Response.status(200).entity("Inserimento conto corrente avvenuto con successo").build();	//status 200 significa OK
	}
	
	// http://localhost:8080/ProgettoSettimana8/rest/ewallet/movimento
	@POST
	@Path("/movimento")
	@Consumes(MediaType.APPLICATION_JSON)	//Consumes e' per passare parametri
	public Response insertMovimento(Movimento movimento) {
		if(movimento.getImporto()<0) {	//controllo che l'importo non sia negativo
			return Response.status(406).entity("l'importo deve essere >=0").build();
		}
		for(ContoCorrente c: conti) {
			if(c.getIban().equals(movimento.getIbanConto())) {
				if(movimento.getTipo().equals(TipoMovimento.VERSAMENTO)) {	//controllo se e' un versamento
					c.versa(movimento.getImporto());
				}
				if(movimento.getTipo().equals(TipoMovimento.PRELIEVO)) {	//controllo se e' un prelievo
					if(c.getSaldo()>=movimento.getImporto()) {
						c.preleva(movimento.getImporto());
					} else {
						return Response.status(406).entity("Non hai abbastanza soldi sul conto per prelevare "+movimento.getImporto()).build();	//406 Not Acceptable
					}
				}
				movimento.setId(idMovimentoSerial++);
				movimenti.add(movimento);
				return Response.status(200).entity("Inserimento movimento avvenuto con successo").build();	//status 200 significa OK
			}
		}
		return Response.status(404).entity("Conto corrente non trovato").build();	//404 Not Found
	}
	
	//Eliminazione
	// http://localhost:8080/ProgettoSettimana8/rest/ewallet
	@DELETE
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)	
	public Response eliminaConto(ContoCorrente conto) {
		for(ContoCorrente c:conti) {
			if(c.getIban().equals(conto.getIban())) {
				conti.remove(c);	//rimuovo il conto corrente dai conti
				for(Movimento m: movimenti) {
					if(m.getIbanConto().equals(c.getIban())) {
						movimenti.remove(m);	//elimino tutti i movimenti di quel conto
					}
				}
				return Response.status(200).entity("Eliminazione avvenuta con successo").build();
			}
		}
		return Response.status(404).entity("Conto corrente non trovato").build();
	}
	
	//Aggiornamenti
	// http://localhost:8080/ProgettoSettimana8/rest/ewallet
	@PUT
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response aggiornaConto(ContoCorrente conto) {
		for(int i=0; i<conti.size();i++) {
			if(conti.get(i).getIban().equals(conto.getIban())) {
				conti.set(i, conto);
				return Response.status(200).entity("Aggiornamento conto corrente avvenuto con successo").build();
			}
		}
		return Response.status(404).entity("Conto corrente non trovato").build();
	}

}
