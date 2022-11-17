import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Set;


public class ej2 {
    private static String rutaLectura = "regJuegos.dat";
    private static String rutaEscritura = "regJuegos(2).dat";
    private static Path path = Paths.get(rutaEscritura);

    public static void main(String[] args){
        duplicar();
    }

    public static void duplicar(){
        try (RandomAccessFile raf = new RandomAccessFile(rutaLectura,"rw");
            FileChannel channelLectura = raf.getChannel();
            FileChannel channelEscritura = FileChannel.open(path,StandardOpenOption.CREATE,StandardOpenOption.APPEND);
        ){
            ByteBuffer buffer = ByteBuffer.allocate(10);
            Charset charset = Charset.forName("US-ASCII");
            while(channelLectura.read(buffer) > 0){
                buffer.flip();
                channelEscritura.write(buffer);
                buffer.clear();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}