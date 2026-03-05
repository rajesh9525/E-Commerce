package E_commers.model;

import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Transient;

@Entity
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "product_name")
	private String productName;
	
	private String productdetails;
	private double productprice;
	private String status;
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	private LocalDate addproductdate;
	
	@Lob
	private byte[] productimage;
	
	@Transient   // 🔥 VERY IMPORTANT
	private MultipartFile file;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductdetails() {
		return productdetails;
	}

	public void setProductdetails(String productdetails) {
		this.productdetails = productdetails;
	}

	public double getProductprice() {
		return productprice;
	}

	public void setProductprice(double productprice) {
		this.productprice = productprice;
	}

	public LocalDate getAddproductdate() {
		return addproductdate;
	}

	public void setAddproductdate(LocalDate addproductdate) {
		this.addproductdate = addproductdate;
	}

	public byte[] getProductimage() {
		return productimage;
	}

	public void setProductimage(byte[] productimage) {
		this.productimage = productimage;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}
	
	
	
}
