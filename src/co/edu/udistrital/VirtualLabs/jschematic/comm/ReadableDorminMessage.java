/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.edu.udistrital.VirtualLabs.jschematic.comm;

import org.jdom.Document;

/**
 *
 * @author Oscar E. Cala W.
 */
public interface ReadableDorminMessage {

        /**
         * Metodo que encapsula en algoritmo que se ejecuta cuando se recibe un mensaje
         * Todas las clases que implementen esta interfaz deben incluir la logica de
         * negocio propia de cada mensaje dentro de este metodo.
         *
         * Pej: Al recibir un mensaje del Tipo StartStatedCreated, debe hacerse re-set
         * de los Widgets, y limpiado de itnerfaz. Referirse al CTAT-TTAI
         * (CATT Tool-Tutor Application Interface) para mayor informacion
         */
        public void dispatch();

        /**
         *
         * Este metodo permite inicializar las propedades del objeto que representa
         * un mensaje que se recibe desde el tutor.
         *
         * @param docs Representacion JDOM del Documento XML que se recibe como mensaje
         */
        public void init(Document docs);
}
