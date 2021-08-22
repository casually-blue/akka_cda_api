package views

import scalatags.Text.all._
import scalatags.Text.tags2.title

object Home extends ScalaPage(
  html(lang := "en") {
    head {
      title {
        "Home Page"
      }
    }
    body {
      h1 {
        "Main Page"
      }
    }
  }
)
