package views

class ScalaPage(html: scalatags.Text.TypedTag[String]) {

  def render: String = "<!DOCTYPE html>" + html
}
