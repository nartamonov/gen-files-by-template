trait TemplateEngine {
  trait Template {
    def render(attributes: Map[String,String]): String
  }

  def createTemplateFromString(s: String): Template
}

object SimpleTemplateEngine extends TemplateEngine {
  class SimpleTemplate(source: String) extends Template {
    private val placeholder = """\{\{\s*(\w+)\s*\}\}""".r

    def render(attributes: Map[String, String]): String = {
      placeholder.replaceAllIn(source, m => {
        val attrName = m.group(1)
        attributes.get(attrName) match {
          case Some(v) => v
          case None => m.matched
        }
      })
    }
  }

  def createTemplateFromString(s: String): Template = new SimpleTemplate(s)
}
