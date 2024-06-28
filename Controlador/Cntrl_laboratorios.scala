package Controlador
import Vista.{laboratorios, ventana_principal}

import java.awt.event.{ActionEvent, ActionListener}
import scala.collection.mutable
import Modelo.Laboratorio
import Modelo.Usuario

import javax.swing.JOptionPane
import javax.swing.table.DefaultTableModel


class Cntrl_laboratorios(ventana_lab: laboratorios,usuarioModelo: Usuario) {
  var ventanaPrincipal = new ventana_principal()
  def Iniciar(): Unit = {
    ventana_lab.setTitle("Crear laboratorios")
    ventana_lab.setLocationRelativeTo(null)
    ventana_lab.setVisible(true)
    ventana_lab.setResizable(false)
    ventana_lab.tablalabs.setVisible(false)

    ventana_lab.btn_registrar.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        val codigo = ventana_lab.txt_codigo.getText()
        val nombre = ventana_lab.txt_nombre.getText()
        val capacidad = ventana_lab.txt_capacidad.getText()

        if (codigo.isEmpty() || nombre.isEmpty() || capacidad.isEmpty()) {

          JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE)
        } else {
          val softwares = List(
            "Julia" -> ventana_lab.chbox_Julia.isSelected,
            "NetBeans" -> ventana_lab.chbox_NetBeans.isSelected,
            "Otro" -> ventana_lab.chbox_Otro.isSelected,
            "VScode" -> ventana_lab.chbox_VScode.isSelected,
            "IntelliJ" -> ventana_lab.chbox_intellij.isSelected
          ).collect { case (software, seleccionado) if seleccionado => software }
            val laboratorioModelo = new Laboratorio()
            laboratorioModelo.crearLaboratorio(codigo, nombre, capacidad, softwares)
            println(s"Se registró el laboratorio: $laboratorioModelo")
            actualizarTablaLaboratorios()
            limpiarCampos()
          }
        }

    })

    ventana_lab.btn_buscar.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        val codigoBuscar = ventana_lab.txt_buscar.getText()
        val laboratorioModelo = new Laboratorio()
        val laboratorioEncontrado = laboratorioModelo.buscarLaboratorioPorCodigo(codigoBuscar)
        laboratorioEncontrado.foreach { laboratorio =>
          ventana_lab.txt_codigo.setText(laboratorio("codigo"))
          ventana_lab.txt_nombre.setText(laboratorio("nombre"))
          ventana_lab.txt_capacidad.setText(laboratorio("capacidad"))
          ventana_lab.chbox_Julia.setSelected(laboratorio("softwares").contains("Julia"))
          ventana_lab.chbox_NetBeans.setSelected(laboratorio("softwares").contains("NetBeans"))
          ventana_lab.chbox_Otro.setSelected(laboratorio("softwares").contains("Otro"))
          ventana_lab.chbox_VScode.setSelected(laboratorio("softwares").contains("VScode"))
          ventana_lab.chbox_intellij.setSelected(laboratorio("softwares").contains("IntelliJ"))
        }
      }
    })

    ventana_lab.btn_modificar.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        val codigoModificar = ventana_lab.txt_buscar.getText()
        val laboratorioModelo = new Laboratorio()
        val laboratorioEncontrado = laboratorioModelo.buscarLaboratorioPorCodigo(codigoModificar)

        laboratorioEncontrado.foreach { laboratorio =>
          val nuevoLaboratorio = mutable.Map(
            "codigo" -> codigoModificar,
            "nombre" -> ventana_lab.txt_nombre.getText(),
            "capacidad" -> ventana_lab.txt_capacidad.getText(),
            "softwares" -> List(
              "Julia" -> ventana_lab.chbox_Julia.isSelected,
              "NetBeans" -> ventana_lab.chbox_NetBeans.isSelected,
              "Otro" -> ventana_lab.chbox_Otro.isSelected,
              "VScode" -> ventana_lab.chbox_VScode.isSelected,
              "IntelliJ" -> ventana_lab.chbox_intellij.isSelected
            ).collect { case (software, seleccionado) if seleccionado => software }.mkString(",")
          )
          if (laboratorioModelo.modificarLaboratorio(codigoModificar, nuevoLaboratorio)) {
            JOptionPane.showMessageDialog(ventana_lab, "Laboratorio modificado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE)
            actualizarTablaLaboratorios()
          } else {
            JOptionPane.showMessageDialog(ventana_lab, "Error al modificar el Laboratorio", "Error", JOptionPane.ERROR_MESSAGE)
          }
        }
        limpiarCampos()
      }
    })

    ventana_lab.btn_eliminar.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        val codigoEliminar = ventana_lab.txt_buscar.getText()
        val laboratorioModelo = new Laboratorio()
        if (laboratorioModelo.eliminarLaboratorio(codigoEliminar)) {
          actualizarTablaLaboratorios()
        } else {
          JOptionPane.showMessageDialog(ventana_lab, "Error al elimnar el Laboratorio", "Error", JOptionPane.ERROR_MESSAGE)
        }
        limpiarCampos()
      }
    })

    ventana_lab.btn_cancelar.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        limpiarCampos()
      }
    })
    ventana_lab.btn_ver.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        val laboratorioModelo = new Laboratorio()
        laboratorioModelo.cargarLaboratorios()

        val todosLosLaboratorios = laboratorioModelo.obtenerLaboratoriss()
        val modeloTabla = new DefaultTableModel()

        modeloTabla.addColumn("Código")
        modeloTabla.addColumn("Nombre")
        modeloTabla.addColumn("Capacidad")
        for (laboratorio <- todosLosLaboratorios) {
          modeloTabla.addRow(Array[AnyRef](
            laboratorio("codigo").asInstanceOf[AnyRef],
            laboratorio("nombre").asInstanceOf[AnyRef],
            laboratorio("capacidad").asInstanceOf[AnyRef]
          ))
        }

        ventana_lab.tablalabs.setModel(modeloTabla)
        ventana_lab.tablalabs.setVisible(true)
      }
    })


    ventana_lab.btn_regresar.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        val c = new Cntrl_VentanaPrincipal(ventanaPrincipal, usuarioModelo)
        c.Iniciar()
        ventana_lab.dispose()
      }
    })
  }

  private def limpiarCampos(): Unit = {
    ventana_lab.txt_codigo.setText("")
    ventana_lab.txt_nombre.setText("")
    ventana_lab.txt_capacidad.setText("")
    ventana_lab.txt_buscar.setText("")
    ventana_lab.chbox_Julia.setSelected(false)
    ventana_lab.chbox_NetBeans.setSelected(false)
    ventana_lab.chbox_Otro.setSelected(false)
    ventana_lab.chbox_VScode.setSelected(false)
    ventana_lab.chbox_intellij.setSelected(false)
  }

  private def actualizarTablaLaboratorios(): Unit = {
    val laboratorioModelo = new Laboratorio()
    laboratorioModelo.cargarLaboratorios()

    val todosLosLaboratorios = laboratorioModelo.obtenerLaboratoriss()
    val modeloTabla = new DefaultTableModel()

    modeloTabla.addColumn("Código")
    modeloTabla.addColumn("Nombre")
    modeloTabla.addColumn("Capacidad")
    for (laboratorio <- todosLosLaboratorios) {
      modeloTabla.addRow(Array[AnyRef](
        laboratorio("codigo").asInstanceOf[AnyRef],
        laboratorio("nombre").asInstanceOf[AnyRef],
        laboratorio("capacidad").asInstanceOf[AnyRef]
      ))
    }

    ventana_lab.tablalabs.setModel(modeloTabla)
    ventana_lab.tablalabs.setVisible(true)
  }
}


