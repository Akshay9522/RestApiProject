package resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Utils {

	public static RequestSpecification req;
	public static String currDir;
	public static XSSFWorkbook workbook;
	public static XSSFSheet sheet;
	public RequestSpecification requestSpecification() throws IOException
	{
		
		if(req==null)
		{
		PrintStream log =new PrintStream(new FileOutputStream("logging.txt"));
		 req=new RequestSpecBuilder().setBaseUri(getGlobalValue("baseUrl")).addQueryParam("key", "qaclick123")
				 .addFilter(RequestLoggingFilter.logRequestTo(log))
				 .addFilter(ResponseLoggingFilter.logResponseTo(log))
		.setContentType(ContentType.JSON).build();
		 return req;
		}
		return req;
		
		
	}
	
	
	public static String getGlobalValue(String key) throws IOException
	{
		Properties prop =new Properties();
		currDir = System.getProperty("user.dir");
		FileInputStream fis =new FileInputStream(currDir+"/src/test/java/resources/global.properties");
		prop.load(fis);
		return prop.getProperty(key);	
	}
	
	
	public String getJsonPath(Response response,String key)
	{
		  String resp=response.asString();
		JsonPath   js = new JsonPath(resp);
		return js.get(key).toString();
	}
	
	public String objectToStringConv(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(obj);
        return json;
	}
public String updateReadJson(String path, String name, String language, String address) throws Throwable {
	JSONObject jsonObject = new JSONObject();
	jsonObject.put("name", name); 
    jsonObject.put("language", language);
    jsonObject.put("address", address); 
    try (FileWriter file = new FileWriter(path)) {
        file.write(jsonObject.toString(4));
        System.out.println("JSON file updated successfully!");
    } catch (IOException e) {
        e.printStackTrace();
    }
		File fl = new File(path);
		FileInputStream fis = new FileInputStream(fl);
		JSONTokener jt = new JSONTokener(fis);
		JSONObject obj = new JSONObject(jt);
		ObjectMapper obj1 =new ObjectMapper();
		String data=obj1.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		return data;
	}
public static  int rowCount(String path, String sheetName) throws Throwable {		
	File file = new File(path);
	FileInputStream fr = new FileInputStream(file);
	workbook = new XSSFWorkbook(fr);
	sheet = workbook.getSheet(sheetName);
	int rowCount = sheet.getPhysicalNumberOfRows();
	return rowCount;
	}

public static String readCell(String path, String sheetName, int rowNumber, int cellNumber) throws Throwable {		
	File file = new File(path);
	FileInputStream fr = new FileInputStream(file);
	workbook = new XSSFWorkbook(fr);
	sheet = workbook.getSheet(sheetName);
	XSSFRow row = sheet.getRow(rowNumber);
	XSSFCell cell = row.getCell(cellNumber);
	return cell.getStringCellValue();
	}

public static void writeCell(String path, String sheetName, int rowNumber, int cellNumber,String Value) throws Throwable {		
	File file = new File(path);
	FileInputStream fr = new FileInputStream(file);
	workbook = new XSSFWorkbook(fr);
	sheet = workbook.getSheet(sheetName);
	XSSFRow row = sheet.createRow(rowNumber);
	row.createCell(cellNumber).setCellValue(Value);		
	}
}
