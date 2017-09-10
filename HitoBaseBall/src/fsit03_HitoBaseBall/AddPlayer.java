package fsit03_HitoBaseBall;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.oreilly.servlet.MultipartRequest;

//import com.oreilly.servlet.MultipartRequest;

@WebServlet("/AddPlayer")
public class AddPlayer extends HttpServlet {
	HttpSession session;
	String id;
	private HttpServletRequest request;
	private List<FileItem> items;
	private LinkedList<PlayerModel> players;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
		request = req;
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		request.setCharacterEncoding("UTF-8");
		session = request.getSession();
		id  = (String)session.getAttribute("teamId");
		players = doDo();
		Players player;
		try {
			player = new Players();
			player.newInsert(players);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
		}
		
	}
	
	private LinkedList<PlayerModel> doDo() {
		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();

		// Configure a repository (to ensure a secure temp location is used)
		ServletContext servletContext = this.getServletConfig().getServletContext();
		File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
		factory.setRepository(repository);

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		// Parse the request
		try {
			items = upload.parseRequest(request);
			System.out.println(items.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
		}
		
		// Process the uploaded items
		Iterator<FileItem> iter = items.iterator();
		int dataCount = 1;
		LinkedList<LinkedList<String>> players = new LinkedList<>();
		LinkedList<String> tmp = new LinkedList<>();
		while (iter.hasNext()) {
		    FileItem item = iter.next();
		    String fileName = item.getName();
		    String value =item.getString();
		    String key = item.getFieldName();
		    //System.out.println("n:"+dataCount+" fileName:"+fileName+" value:"+value+" key:" + key);
		    if(fileName != null) {
		    	tmp.add(key);
		    	//System.out.println(tmp.size() +" : " + key);
		    }else {
		    	tmp.add(value);
		    }
		    if (value.equals("end")) {
		    	LinkedList<String> player = new LinkedList<>();
		    	for(int i = 0; i < tmp.size(); i ++) {
		    		player.add(tmp.get(i));
		    	}

		    	tmp = new LinkedList<>();
		    	players.add(player);
		    	dataCount++;
		    	System.out.println(players.size());
		    }
		}
		
		LinkedList<PlayerModel> ps = new LinkedList<>();
		for(int i = 0; i < players.size(); i++) {
			LinkedList<String> tmpPlayer = players.get(i);
			PlayerModel player = new PlayerModel();
			for(int j = 0; j < tmpPlayer.size() - 1; j++) {
				switch(j) {
					case 0:
						player.setTemid(id);
						player.setNumber(tmpPlayer.get(j));
						break;
					case 1:
						player.setName(tmpPlayer.get(j));
						break;
					case 2:
						player.setHeight(tmpPlayer.get(j));
						break;
					case 3:
						player.setWeight(tmpPlayer.get(j));
						break;
					case 4:
						player.setBirthday(tmpPlayer.get(j));
						break;
					case 5:
						tmpPhoto(player.getNumber(), tmpPlayer.get(j));
						System.out.println("55");
						break;
					case 6:
						player.setThr(tmpPlayer.get(j));
						break;
					case 7:
						player.setHit(tmpPlayer.get(j));
						break;
					case 8:
						player.setPosition(tmpPlayer.get(j));
						break;
					default:
						StringBuilder sb = new StringBuilder();
						sb.append(player.getPosition()+"/"+tmpPlayer.get(j));
						player.setPosition(sb.toString());
				}
				
			}
			ps.add(player);
		}
		
		return ps;
	}
	
	private void tmpPhoto(String pnumber, String bytes) {
		try {
			String uploadPath = 
					getServletContext().getInitParameter("upload-path");
			String tmpFileName = uploadPath + id +"_"+pnumber+".png";
			OutputStream out = new FileOutputStream(tmpFileName);
			out.write(bytes.getBytes());
			out.flush();
			out.close();
			System.out.println(pnumber);
//			//取得teamid
//			
//			
//			
//			String strFile = (String)items.nextElement();
//			File file = req.getFile(strFile);
//			String FSName = req.getFilesystemName(strFile);
//			String upName = req.getOriginalFileName(strFile);
//			//out.println(upName + ":"+id+"←"+"  lenght:");
//
//
//			//檔名變更//teamid_logo.jpg
//			String srcFilename = uploadPath+upName;
//			String destFilename = uploadPath+id+"_"+pname+".png"; 
//			boolean filechange = FileReName.rename(srcFilename, destFilename);
		  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
		}
	}
}
	 




