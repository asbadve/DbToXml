
    /**
     * @param args the command line arguments
     */
import java.awt.Desktop;
import java.awt.HeadlessException;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.sql.*;
import javax.swing.JFrame;
import javax.swing.JList;
public class main extends JFrame{
   
  public static void main(String args[]) throws Exception ,IOException
  {
    Connection con=null,con1=null;
    Statement stm,stm1;
    File f1;
    new main();
        try
        {
        con1=DriverManager.getConnection("jdbc:mysql://localhost/","root","");
	stm1=con1.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
        }
        catch(Exception e)
	{
	e.printStackTrace();
	}
        PreparedStatement ps_database;
        ps_database=con1.prepareStatement("show databases");
        ResultSet rs_database=ps_database.executeQuery("show databases");
        System.out.println("Databases presents in mysql Database are:-");
        while(rs_database.next())
        {
            System.out.println(rs_database.getString(1));
    
        }
    con1.close();
    //rs_database.close();
    BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
    System.out.println("Please enter the correct database name that you want to convert to xml:-");
    String databaseName= br.readLine();
    System.out.println("Database name:-"+databaseName);
    
      
      
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.newDocument();
    Element results = doc.createElement(databaseName);
    doc.appendChild(results);
    //<editor-fold defaultstate="collapsed" desc="for the second file">
    DocumentBuilderFactory factory1 = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder1 = factory1.newDocumentBuilder();
    Document doc1 = builder1.newDocument();
    Element results1 = doc1.createElement(databaseName);
    doc1.appendChild(results1);
      
    //</editor-fold>
    
    
    
    try
        {
        con=DriverManager.getConnection("jdbc:mysql://localhost/"+databaseName+"","root","");
	stm=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
        }
        catch(Exception e)
	{
	e.printStackTrace();
	}
       
    
        
//////////////////////////////////For the table count
    PreparedStatement ps,ps2 ;
    //ps=con.prepareStatement("select count(*) tables from information_schema.tables where table_schema='s'");
    //ResultSet rs3=ps.executeQuery("select count(*) tables from information_schema.tables where table_schema='s'");
    //int rowCount = 0;
    //<editor-fold defaultstate="collapsed" desc="following while loop is for the count of the table present in given database">
    
    //</editor-fold>
      //  while(rs3.next())
        //{
          //  rowCount = rs3.getInt(1);
    
        //}
        //System.out.println("Number of tables in databse is:-"+rowCount);
//////////////////////////////////For the table name
    
    ps2=con.prepareStatement("show tables");
    ResultSet rs4=ps2.executeQuery("show tables");
    String  tableName;
    //String arrytname[]=null;
    
    
    
    while(rs4.next())
         {
            tableName = rs4.getString(1);
          //String arrytname [] =rs4.getArray(1);
     
    //           arryTableName =tableName
      //      System.out.println("table name in a while loop:-"+tableName);
    
            ResultSet rs1 = con.createStatement().executeQuery("desc "+tableName);
            ResultSetMetaData rsmd1 = rs1.getMetaData();
            int colCount1 = rsmd1.getColumnCount();
    //System.out.println(colCount);
            //<editor-fold defaultstate="collapsed" desc="This while loop is for the respective tables data convert to xml">
            
            //</editor-fold>
            while (rs1.next())
            {
                //<editor-fold defaultstate="collapsed" desc="Adding the table name in element">
                
                //</editor-fold>

            Element row1 = doc1.createElement(tableName);
            //<editor-fold defaultstate="collapsed" desc="Adding the table name element in database">
            
            //</editor-fold>
            results1.appendChild(row1);
            for (int i = 1; i <= colCount1; i++) 
                {
                    try
                    {        
                    String columnName1 = rsmd1.getColumnName(i);
                    System.out.println("Colm name:-"+rsmd1.getColumnName(i));
                    
                    Object value1 = rs1.getObject(i);
                    System.out.println("Obect name:-"+rs1.getObject(i));
                    
                    Element node1 = doc1.createElement(columnName1);
                    //if(value1.toString().isEmpty())
                    if("Null".equals(columnName1.toString()))
                    {
                        System.out.println("In IF loop for the null colname for null:-");
                    }
                    
                
                    node1.appendChild(doc1.createTextNode(value1.toString()));
                    
                    
                    row1.appendChild(node1);
                    }
                    catch(NullPointerException e1)
                    {
                        e1.getMessage();
                    }
                }
            }
        }    
    DOMSource domSource1 = new DOMSource(doc1);
    TransformerFactory tf1 = TransformerFactory.newInstance();
    Transformer transformer1 = tf1.newTransformer();
   
    transformer1.setOutputProperty(OutputKeys.METHOD,"xml");
   
    transformer1.setOutputProperty(OutputKeys.ENCODING,"ISO-8859-1");
    
    StringWriter sw1 = new StringWriter();
    StreamResult sr1 = new StreamResult(sw1);
    transformer1.transform(domSource1,sr1);
    
    System.out.println(sw1.toString());
    ///<editor-fold defaultstate="collapsed" desc="Now the copy of string to another string for the purpose of writing in another file">
    //</editor-fold>
    String xmlContent1 = sw1.toString();
    File fileForXml1 = new File("./xmlfiles/schema.xml");
    BufferedWriter bw1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileForXml1)));
   
    bw1.write(xmlContent1);
    bw1.flush();
    bw1.close();
    System.out.println("xml file successfully created file.....");
    String path1="xmlfiles/schema.xml";
    try
      {
          Desktop.getDesktop().open(new File(path1));
      } catch (Exception e2) 
      {
          e2.printStackTrace();
      }  
    
    ps=con.prepareStatement("show tables");
    ResultSet rs5=ps.executeQuery("show tables");
    String  tableName1;
     while(rs5.next())
        {
            System.out.println("in next file");
            tableName = rs5.getString(1);
            System.out.println("table name in a while loop:-"+tableName);
    
            ResultSet rs = con.createStatement().executeQuery("select * from "+tableName);
            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();
   
            //<editor-fold defaultstate="collapsed" desc="This while loop is for the respective tables data convert to xml">
            
            //</editor-fold>
            while (rs.next())
            {
                //<editor-fold defaultstate="collapsed" desc="Adding the table name in element">
                
                //</editor-fold>

            Element row = doc.createElement(tableName);
            //<editor-fold defaultstate="collapsed" desc="Adding the table name element in database">
            
            //</editor-fold>
            results.appendChild(row);
            for (int i = 1; i <= colCount; i++) 
                {
                String columnName = rsmd.getColumnName(i);
                Object value = rs.getObject(i);
                Element node = doc.createElement(columnName);
                node.appendChild(doc.createTextNode(value.toString()));
                row.appendChild(node);
                }
            }
        }
    DOMSource domSource = new DOMSource(doc);
    TransformerFactory tf = TransformerFactory.newInstance();
    Transformer transformer = tf.newTransformer();
    transformer.setOutputProperty(OutputKeys.METHOD,"xml");
    transformer.setOutputProperty(OutputKeys.ENCODING,"ISO-8859-1");
    
    StringWriter sw = new StringWriter();
    StreamResult sr = new StreamResult(sw);
    transformer.transform(domSource,sr);
    
    System.out.println(sw.toString());
    ///<editor-fold defaultstate="collapsed" desc="Now the copy of string to another string for the purpose of writing in another file">
    //</editor-fold>
    String xmlContent = sw.toString();
    File fileForXml = new File("./xmlfiles/newxml.xml");
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileForXml)));
    bw.write(xmlContent);
    bw.flush();
    bw.close();
    System.out.println("xml file successfully created file.....");
    String path;
    path = "xmlfiles/newxml.xml";
    con.close();
    try
      {
          Desktop.getDesktop().open(new File(path));
      } catch (Exception e2) 
      {
          e2.printStackTrace();
      }
   
   }


}