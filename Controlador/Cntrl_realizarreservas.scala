package Controlador
import Modelo.Laboratorio
import Modelo.Reserva
import Modelo.Usuario
import Vista.{realizarreservas, ventana_principal}

import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.JOptionPane
import javax.swing.table.DefaultTableModel
class Cntrl_realizarreservas(ventana_reservar: realizarreservas,usuarioModelo: Usuario) {
  var ventanaPrincipal = new ventana_principal()
  var laboratorioModelo = new Laboratorio
  var reservaModelo = new Reserva()

  def Iniciar(): Unit = {
    ventana_reservar.setTitle("Realizar Reserva de laboratorio")
    ventana_reservar.setLocationRelativeTo(null)
    ventana_reservar.setVisible(true)
    ventana_reservar.setResizable(false)
    ventana_reservar.tablareservas.setVisible(false)
    val laboratorios = laboratorioModelo.obtenerLaboratoriss()
    laboratorios.foreach(laboratorio => ventana_reservar.cbox_labs.addItem(s"${laboratorio("codigo")} - ${laboratorio("nombre")}"))

    ventana_reservar.btn_reservar.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        val laboratorioSeleccionado = ventana_reservar.cbox_labs.getSelectedItem.toString.split(" - ")(0) // Obtener el código del laboratorio seleccionado
        val fecha = s"${ventana_reservar.cbox_dia.getSelectedItem}-${ventana_reservar.cbox_mes.getSelectedItem}-${ventana_reservar.cbox_anio.getSelectedItem}"
        val hora = ventana_reservar.Hora.getSelectedItem.toString
        val estado = "pendiente"
        if (laboratorioSeleccionado.isEmpty || fecha.isEmpty || hora.isEmpty) {
          JOptionPane.showMessageDialog(ventana_reservar, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE)
        } else {
          reservaModelo.hacerReserva(laboratorioSeleccionado, fecha, hora, estado)
          JOptionPane.showMessageDialog(ventana_reservar, "Reserva realizada con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE)
        }

      }
    })

    ventana_reservar.btn_ver.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        val reservaModelo = new Reserva()
        reservaModelo.cargarReservasPendientes()
        reservaModelo.cargarReservasAprobadas()

        val reservasPendientes = reservaModelo.obtenerReservasPendientes()
        val reservasAprobadas = reservaModelo.obtenerReservasAprobadas()

        val modeloTabla = new DefaultTableModel()
        modeloTabla.addColumn("Laboratorio")
        modeloTabla.addColumn("Fecha")
        modeloTabla.addColumn("Hora")
        modeloTabla.addColumn("Estado")

        // Añadir filas al modelo para reservas pendientes
        for (reserva <- reservasPendientes) {
          modeloTabla.addRow(Array[AnyRef](
            reserva("laboratorio").asInstanceOf[AnyRef],
            reserva("fecha").asInstanceOf[AnyRef],
            reserva("hora").asInstanceOf[AnyRef],
            reserva("estado").asInstanceOf[AnyRef]
          ))
        }
        for (reserva <- reservasAprobadas) {
          modeloTabla.addRow(Array[AnyRef](
            reserva("laboratorio").asInstanceOf[AnyRef],
            reserva("fecha").asInstanceOf[AnyRef],
            reserva("hora").asInstanceOf[AnyRef],
            "Aprobada".asInstanceOf[AnyRef] // Agregar estado específico para reservas aprobadas
          ))
        }

        ventana_reservar.tablareservas.setModel(modeloTabla)

        ventana_reservar.tablareservas.setVisible(true)
      }
    })


    ventana_reservar.btn_regresar.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        val c = new Cntrl_VentanaPrincipal(ventanaPrincipal, usuarioModelo)

        c.Iniciar()
        ventana_reservar.dispose()
      }
    })
  }
}

