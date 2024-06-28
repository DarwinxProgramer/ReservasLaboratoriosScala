package Modelo
import scala.collection.mutable
import scala.util.{Try, Success, Failure}
import java.io.{ObjectInputStream, ObjectOutputStream}
import java.nio.file.{Files, Paths}
import scala.collection.mutable.ArrayBuffer

class Laboratorio() {
  private var laboratorios: ArrayBuffer[mutable.Map[String, String]] = ArrayBuffer()
  private val filePath: String = "laboratorios.dat"

  cargarLaboratorios()

  def crearLaboratorio(codigo: String, nombre: String, capacidad: String, softwares: List[String]): Unit = {
    val nuevoLaboratorio: mutable.Map[String, String] = mutable.Map(
      "codigo" -> codigo,
      "nombre" -> nombre,
      "capacidad" -> capacidad,
      "softwares" -> softwares.mkString(",")
    )
    laboratorios += nuevoLaboratorio
    guardarLaboratorios()
  }

  def buscarLaboratorioPorCodigo(codigo: String): Option[mutable.Map[String, String]] = {
    cargarLaboratorios()
    laboratorios.find { laboratorio => laboratorio("codigo") == codigo }
  }

  def modificarLaboratorio(codigo: String, nuevoLaboratorio: mutable.Map[String, String]): Boolean = {
    laboratorios.indexWhere { laboratorio => laboratorio("codigo") == codigo } match {
      case -1 => false
      case index =>
        laboratorios.update(index, nuevoLaboratorio)
        guardarLaboratorios()
        true
    }
  }

  def eliminarLaboratorio(codigo: String): Boolean = {
    laboratorios.indexWhere { laboratorio => laboratorio("codigo") == codigo } match {
      case -1 => false
      case index =>
        laboratorios.remove(index)
        guardarLaboratorios()
        true
    }
  }

  def guardarLaboratorios(): Try[Unit] = {
    Try {
      val fileOutputStream = new ObjectOutputStream(Files.newOutputStream(Paths.get(filePath)))
      fileOutputStream.writeObject(laboratorios)
      fileOutputStream.close()
    }
  }

  def cargarLaboratorios(): Unit = {
    if (Files.exists(Paths.get(filePath))) {
      Try {
        val fileInputStream = new ObjectInputStream(Files.newInputStream(Paths.get(filePath)))
        laboratorios = fileInputStream.readObject().asInstanceOf[ArrayBuffer[mutable.Map[String, String]]]
        fileInputStream.close()
      } match {
        case Success(_) =>
        case Failure(ex) => ex.printStackTrace()
      }
    }
  }

  def obtenerLaboratoriss(): ArrayBuffer[mutable.Map[String, String]] = {
    laboratorios
  }
}
