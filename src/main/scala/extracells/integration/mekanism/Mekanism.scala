package extracells.integration.mekanism

import appeng.api.AEApi
import extracells.api.ECApi
import mekanism.api.gas.{ITubeConnection, IGasHandler, GasRegistry, Gas}
import net.minecraftforge.fluids.{FluidRegistry, FluidStack, Fluid}
import scala.collection.JavaConversions._


object Mekanism {

  private var fluidGas:Map[Gas, Fluid] = Map()

  def init {
    val api = AEApi.instance.partHelper
    api.registerNewLayer(classOf[LayerGasHandler].getName, classOf[IGasHandler].getName)
    api.registerNewLayer(classOf[LayerTubeConnection].getName, classOf[ITubeConnection].getName)
    AEApi.instance().registries().cell().addCellHandler(new GasCellHandler())
  }

  def getFluidGasMap  = mapAsJavaMap(fluidGas)

  def postInit{
    val it = GasRegistry.getRegisteredGasses.iterator
    while(it.hasNext){
      val g = it.next
      val fluid = new GasFluid(g)
      fluidGas += (g -> fluid)
    }
    ECApi.instance.addFluidToShowBlacklist(classOf[GasFluid])
    ECApi.instance.addFluidToStorageBlacklist(classOf[GasFluid])
  }

  class GasFluid(gas: Gas) extends Fluid("ec.internal." + gas.getName){

    FluidRegistry.registerFluid(this)

    override def getLocalizedName (stack: FluidStack) =  gas.getLocalizedName

    override def getIcon = gas.getIcon

    override def getStillIcon = gas.getIcon

    override def getFlowingIcon = gas.getIcon

    def getGas = gas
  }

}
