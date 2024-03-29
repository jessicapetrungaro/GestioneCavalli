package it.prova.gestionecavalli.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.prova.gestionecavalli.model.Cavallo;
import it.prova.gestionecavalli.service.MyServiceFactory;
import it.prova.gestionecavalli.utility.UtilityCavalloForm;

@WebServlet("/ExecuteInsertCavalloServlet")
public class ExecuteInsertCavalloServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ExecuteInsertCavalloServlet() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// estraggo input
		String nomeInputParam = request.getParameter("nome");
		String razzaInputParam = request.getParameter("razza");
		String prezzoInputStringParam = request.getParameter("prezzo");
		String dataDiNascitaStringParam = request.getParameter("datadinascita");

		// preparo un bean (che mi serve sia per tornare in pagina
		// che per inserire) e faccio il binding dei parametri
		Cavallo cavalloInstance = UtilityCavalloForm.createCavalloFromParams(nomeInputParam,
				razzaInputParam, prezzoInputStringParam, dataDiNascitaStringParam);

		// se la validazione non risulta ok
		if (!UtilityCavalloForm.validateCavalloBean(cavalloInstance)) {
			request.setAttribute("insert_cavallo_attr", cavalloInstance);
			request.setAttribute("errorMessage", "Attenzione sono presenti errori di validazione");
			request.getRequestDispatcher("cavallo/insert.jsp").forward(request, response);
			return;
		}

		// se sono qui i valori sono ok quindi posso creare l'oggetto da inserire
		// occupiamoci delle operazioni di business
		try {
			MyServiceFactory.getCavalloServiceInstance().inserisciNuovo(cavalloInstance);
			request.setAttribute("listaCavalliAttribute", MyServiceFactory.getCavalloServiceInstance().listAll());
			request.setAttribute("successMessage", "Operazione effettuata con successo");
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Attenzione si è verificato un errore.");
			request.getRequestDispatcher("/index.jsp").forward(request, response);
			return;
		}

		// andiamo ai risultati
		request.getRequestDispatcher("cavallo/results.jsp").forward(request, response);

	}

}
