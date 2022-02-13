package net.wiringbits.webapp.utils.ui.web.components.pages

import net.wiringbits.facades.reactRouter.mod.useParams
import net.wiringbits.webapp.utils.api.models.AdminGetTableMetadata
import net.wiringbits.webapp.utils.slinkyUtils.components.core.AsyncComponent
import net.wiringbits.webapp.utils.ui.web.API
import net.wiringbits.webapp.utils.ui.web.components.widgets.{ExperimentalTable, Loader}
import org.scalajs.dom.{URLSearchParams, window}
import slinky.core.{FunctionalComponent, KeyAddingStage}

import scala.scalajs.js
import scala.util.Try

object TableMetadataPage {
  case class Props(api: API)

  def apply(api: API): KeyAddingStage = component(Props(api = api))

  val component: FunctionalComponent[Props] = FunctionalComponent[Props] { props =>
    val defaultPageLength = 10
    val defaultOffset = 0

    val urlSearchParams = new URLSearchParams(window.location.search)

    val limit = Try(urlSearchParams.get("limit").toInt).toOption
    val offset = Try(urlSearchParams.get("offset").toInt).toOption

    val tableName = useParams().asInstanceOf[js.Dynamic].tableName.toString

    AsyncComponent.component[AdminGetTableMetadata.Response](
      AsyncComponent
        .Props(
          fetch = () =>
            props.api.client
              .getTableMetadata(tableName, offset.getOrElse(defaultOffset), limit.getOrElse(defaultPageLength)),
          render = response => ExperimentalTable.component(ExperimentalTable.Props(response)),
          progressIndicator = () => Loader()
        )
    )
  }
}