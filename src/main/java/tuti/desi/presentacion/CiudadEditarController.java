package tuti.desi.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import tuti.desi.servicios.CiudadService;
import tuti.desi.servicios.ProvinciaService;


@Controller
public class CiudadEditarController {

	@Autowired
    private CiudadService servicioCiudad;
	@Autowired
    private ProvinciaService servicioProvincia;
    

 
}
