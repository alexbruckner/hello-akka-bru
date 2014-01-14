package ax.bru.defs

import java.util.{Map => JMap}
import org.eintr.loglady.Logging

/**
 * Created by alexbruckner on 14/01/2014.
 */
class Step(val name: String)(var data: JMap[String, Any]) extends Logging {

   var action: Action = null
   val id = Action.getId(this.name)

   def setAction(name: String, parallel: Boolean = false): Action = {
     val action = new Action(name, parallel, null)(data)
     log.debug(s"setting further action $action for step $this")
     this.action = action
     action
   }

   def setExecutable(function: (Data) => Unit) {
     log.debug(s"setting executable function for step $this")
     action = new Action("executable", false, function)(data)
   }

   def hasAction = this.action != null

   def hasFurtherActionSteps = this.hasAction && this.action.hasSteps

   //TODO comment out to avoid confusion...?
   def execute() {
     action.execute()
   }

   override def toString() = s"Step $name"

 }
