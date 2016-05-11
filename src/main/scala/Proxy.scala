import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.{ChannelInitializer, ChannelOption}
import io.netty.example.proxy.HexDumpProxyFrontendHandler
import io.netty.handler.logging.{LogLevel, LoggingHandler}

object Proxy extends App {
  val proxyHost = "localhost"
  val proxyPort = 1080

  def bootstrap(remoteHost: String, remotePort: Int) = {
    val b = new ServerBootstrap
    val bossGroup = new NioEventLoopGroup(1)
    val workerGroup = new NioEventLoopGroup()
    b.group(bossGroup, workerGroup)
    .channel(classOf[NioServerSocketChannel])
    .childHandler(new ChannelInitializer[SocketChannel] {
      def initChannel(ch: SocketChannel) = ch.pipeline
        .addLast(
          new LoggingHandler(LogLevel.INFO),
          new HexDumpProxyFrontendHandler(remoteHost, remotePort, proxyHost, proxyPort))
    })
    .childOption[java.lang.Boolean](ChannelOption.AUTO_READ, false)
    b.bind(remotePort)
  }

  bootstrap("106.186.46.51", 2203)
  bootstrap("106.186.45.249", 2106)
}
