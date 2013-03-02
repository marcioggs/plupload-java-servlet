import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import eu.medsea.util.MimeUtil;

public class UploadServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		doPost(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
			
			try {
				List items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(req);
				
				for (Iterator iter = items.iterator();	iter.hasNext();	) {
					FileItem item = (FileItem) iter.next();

					if (item.isFormField()) {
						// Process regular form field (input type="text|radio|checkbox|etc", select, etc).
						String fieldname = item.getFieldName();
						String fieldvalue = item.getString();
					} else {
						// Process form file field (input type="file").
						String fileName = FilenameUtils.getName(item.getName());
						InputStream fileContent = item.getInputStream();
						
						File file = new File("D:\\" + fileName);
						OutputStream fileOs = new FileOutputStream(file);
						
						IOUtils.copy(fileContent, fileOs);
						String mimeType = MimeUtil.getMagicMimeType(file);
						
						if (!mimeType.equalsIgnoreCase("image/jpeg") && 
						    !mimeType.equalsIgnoreCase("image/png")) {

							fileOs.close();
						    FileUtils.forceDelete(file);
							throw new ServletException();
						}
						
						fileOs.close();
					}
				}
				
			} catch (FileUploadException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
	}

}
