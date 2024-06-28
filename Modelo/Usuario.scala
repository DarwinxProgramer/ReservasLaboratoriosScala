package Modelo

import scala.collection.mutable
import scala.util.Try
import java.io.{File, FileInputStream, FileOutputStream, ObjectInputStream, ObjectOutputStream}
import scala.collection.mutable.ArrayBuffer


class Usuario() {
  private var usuarios: ArrayBuffer[(String, mutable.Map[String, String])] = ArrayBuffer()
  private val filePath: String = "usuarios.dat"
  private var rolUsuarioAutenticado: Option[String] = None
  inicializarUsuarios()


  def crearUsuario(id: String, nombre: String, apellido: String, rol: String, email: String, contrasena: String): Unit = {
    cargarUsuarios()

    val nuevoUsuario: mutable.Map[String, String] = mutable.Map(
      "id" -> id,
      "nombre" -> nombre,
      "apellido" -> apellido,
      "rol" -> rol,
      "email" -> email,
      "contrasena" -> contrasena
    )
    usuarios += id -> nuevoUsuario
    guardarUsuarios()
  }


  def buscarUsuarioPorId(id: String): Option[mutable.Map[String, String]] = {
    cargarUsuarios()
    usuarios.find { case (userId, _) => userId == id }.map(_._2)
  }

  def modificarUsuario(id: String, nuevoUsuario: mutable.Map[String, String]): Boolean = {
    usuarios.indexWhere { case (userId, _) => userId == id } match {
      case -1 => false
      case index =>
        usuarios.update(index, id -> nuevoUsuario)
        guardarUsuarios()
        true
    }
  }

  def eliminarUsuario(id: String): Boolean = {
    usuarios.indexWhere { case (userId, _) => userId == id } match {
      case -1 => false
      case index =>
        usuarios.remove(index)
        guardarUsuarios()
        true
    }
  }

  def guardarUsuarios(): Try[Unit] = {
    Try {
      val fileOutputStream = new FileOutputStream(filePath)
      val objectOutputStream = new ObjectOutputStream(fileOutputStream)
      objectOutputStream.writeObject(usuarios)
      objectOutputStream.close()
    }
  }

   def cargarUsuarios(): Try[Unit] = {
    Try {
      val fileInputStream = new FileInputStream(filePath)
      val objectInputStream = new ObjectInputStream(fileInputStream)
      usuarios = objectInputStream.readObject().asInstanceOf[ArrayBuffer[(String, mutable.Map[String, String])]]
      objectInputStream.close()
    }
  }

  def inicializarUsuarios(): Unit = {
    val file = new File(filePath)
    if (!file.exists()) {
      val adminExists = usuarios.exists { case (userId, _) => userId == "admin" }

      if (!adminExists) {
        usuarios += "admin" -> mutable.Map(
          "id" -> "admin",
          "nombre" -> "admin",
          "apellido" -> "admin",
          "rol" -> "admin",
          "email" -> "admin",
          "contrasena" -> "admin"
        )
        guardarUsuarios()
      }
    }
  }


  def obtenerUsuarios(): ArrayBuffer[(String, mutable.Map[String, String])] = {
    usuarios
  }

  def buscarUsuarioPorEmail(email: String): Option[mutable.Map[String, String]] = {
    usuarios.find { case (_, userData) => userData("email") == email }.map(_._2)
  }

  def verificarCredenciales(email: String, contrasena: String): Boolean = {
    buscarUsuarioPorEmail(email) match {
      case Some(userData) => userData("contrasena") == contrasena
      case None => false
    }
  }

  def obtenerRolUsuario(rol: String): Option[String] = {
    usuarios.find { case (_, userData) => userData("rol") == rol }.map { case (_, userData) =>
      userData("rol")
    }
  }

    def establecerRolUsuarioAutenticado(rol: String): Unit = {
      rolUsuarioAutenticado = Some(rol)
    }

    def obtenerRolUsuarioAutenticado(): Option[String] = {
      rolUsuarioAutenticado
    }


}
