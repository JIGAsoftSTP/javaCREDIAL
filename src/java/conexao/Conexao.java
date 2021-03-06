/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexao;

import bean.LoginBean;
import bean.UtilizadorBean;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.swing.Timer;
import org.primefaces.component.poll.Poll;
import org.primefaces.component.poll.PollHandler;
import org.primefaces.context.RequestContext;

/**
 *
 * @author ahmed
 */
public final class Conexao implements Serializable{

    /**
     * <br/> O endereco local dos servico oracle.
     */
    public static int i = 0;
    public static int j = 0;
    public static int k = 0;
    private static Timer timerTry = null;
    public static String[] vetConect = new String[2];

    private boolean connectado;
    private static String senhaBd;
    private static String nomeBD;
    private static String caminhoBD;
    private static Connection conexao;
    private Statement statement;

    /**
     * Criar uma nova conexao
     *
     */
    
    public Conexao()
    {
       
        try
        {
              
               /*EstadoConnexao.isValid = false;*/
               
            if ((Conexao.caminhoBD == null
                    ||Conexao.nomeBD == null
                    ||Conexao.senhaBd == null
                    ||Conexao.vetConect == null)
                    /*|| EstadoConnexao.isValid == true*/)
            {
                 openFile();
            }
            
            System.err.println(caminhoBD +" Caminho");
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Conexao.conexao = DriverManager.getConnection(caminhoBD,nomeBD,senhaBd);
            hideModal();
            EstadoConnexao.isValid = true;
           System.out.println("Conectado");
                
        }
        catch (Exception e) 
        {
           // System.out.println("Tentativa de conexa falhada");
            Conexao.caminhoBD = null;
            Conexao.nomeBD = null;
            Conexao.senhaBd = null;
            Conexao.vetConect = null;
            //createTimmer();
            //Quando nao conseguir estabelecer a connexo color o estado para conexao falso 
              //Siginifica que nenhuma conexa podera ser estabelecida
            
            //Iniciar o timer que ira rever o estado da conexao
               //Quando coseguir estabelecer a conexa o estado voltara de nova a verdade
          showModal();
//                
//            e.printStackTrace();
        }
        try 
        {
            statement = conexao.createStatement();
            connectado = true;
        } catch (Exception e) {
        
        }
        
        //createTimmer();
    }

    /**
     * Obter a conexao criada
     *
     * @return
     */
    public Connection getCon() {
        return this.conexao;
    }


    /**
     * Fechar a conexoa que se foi aberto
     *
     */
    public String closeConect() {
        String resp = "Fechado";
        if (connectado) {
            try {
                statement.close();
            } catch (Exception e) {
                resp = "Falha ao Fechar Statemen";
            }
            try {
                conexao.close();
            } catch (Exception e){
                resp = "Falha ao fechar conexao";
            }
            statement = null;
            conexao = null;
            connectado = false;
        } else {
            resp = "Nao ha connexao para fexar";
        }
        return resp;
    }

    /**
     * Verificar se esta conectado
     *
     * @return
     */
    public boolean isConectado() {
        return this.connectado;
    }

    //Detruir totadas as infromacoes sobre o estado do ojectoto
    public void desCon() {
        this.closeConect();
        this.statement = null;
        this.conexao = null;
        this.connectado = false;
    }
    
    @SuppressWarnings("SuspiciousIndentAfterControlStatement")
    public static void openFile(){

        String[] file = null;
        
        try {
            
//            String path = new File("BD.txt").getCanonicalPath();
            File f=new File("BDCREDIAL.txt");
            if(!f.exists())
            {
                try (PrintWriter pw = new PrintWriter(f)) {
                    pw.print("jdbc:oracle:thin:@192.168.2.99:1521:XE;CREDIAL;1234");
                }
            }
            //System.out.println(f.getAbsolutePath());
            Scanner scanner=new Scanner(f);
            while (scanner.hasNext()) {
              file=scanner.nextLine().split(";");
            }
            if(file!=null)
            {
                
                Conexao.caminhoBD= file[0];
                Conexao.nomeBD=file[1];
                Conexao.senhaBd=file[2];
                // System.out.println("endereço "+caminhoBD);
            }
            else
            {
              //  System.out.println("ficheiro vazio");
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    
    private static void createTimmer()
    {
        if (Conexao.timerTry == null)
        {
            //Criar um timer que roda de 10 em 10 segundos
            Conexao.timerTry = new Timer(1000*20, (action) ->
            {
                try //Tentar estabelecer a connexao
                {
                    openFile();
                    System.out.println("Tentando Connetar >> INICIADO");
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                    conexao = DriverManager.getConnection(Conexao.caminhoBD, Conexao.nomeBD, Conexao.senhaBd);
                    EstadoConnexao.isValid = true;
                    Conexao.timerTry.stop();
                    System.out.println("Tentando Connetar >> SUCESSO");
                }
                catch(Exception ex) //Falha na tentativa de conexao
                {
                    System.out.println("Tentando Connetar >> INSUCESSO");
                    EstadoConnexao.isValid = false;
                }
            });
        } 
    }
   public static int ii=0;
    private void showModal() {
        if (k != 1) {
            System.out.println("showModal()");
            k = 1;
            j = 0;
          //  RequestContext.getCurrentInstance().execute("mostrar()"); 
        }
    } 
    private void hideModal() {
        if (j != 1) {
            System.out.println("hideModal()");
          //  RequestContext.getCurrentInstance().execute("ocultar()");
            j = 1;
            k = 0;
        }
    } 
}
