package models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.UniqueConstraint;

import play.Logger;
import models.Constants.PaymentMode;
import models.Constants.ReportType;
import models.customer.Customer;
import models.customer.CustomerHotelInfo;
import models.hotel.Hotel;
import models.hotel.HotelAssociate;
import models.vendor.Reservation;
import cache.TimeToSlotMapCache;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Model;
import com.avaje.ebean.PagedList;
import com.fasterxml.jackson.annotation.JsonIgnore;

import controllers.UserController;

@Entity
public class CustomerOrder extends BaseEntity implements Order {

	/**
	 *
	 */
	private static final long serialVersionUID = -2122929631614156114L;

	private transient static final TimeToSlotMapCache slotMap = TimeToSlotMapCache.getInstance();

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	@Column(unique=true)
	private String orderNumber;

	@ManyToOne(fetch = FetchType.EAGER)
	private Associate associate;

	@ManyToOne
	private VendorServices vendorServices;

	@ManyToOne
	private Customer customer;

	@ManyToOne
	private Vendor vendor;
	
	@ManyToOne
	public Hotel hotel;

	@ManyToOne
	public HotelAssociate hotelAssociate;
	
	public Date fromTime;

	public Date toTime;

	@ManyToOne
	private Address address;

	private Float latitude;

	private Float longitude;

	/*@ManyToOne
	private Supervisor supervisor;*/

	public AppUser getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(AppUser createdBy) {
		this.createdBy = createdBy;
	}

	@ManyToOne
	private AppUser createdBy;
	
	private String associateOtp;
	
	private Boolean associateOtpVerified = false;

	/*public Supervisor getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(Supervisor supervisor) {
		this.supervisor = supervisor;
	}*/

	public String getAssociateOtp() {
		return associateOtp;
	}

	public void setAssociateOtp(String associateOtp) {
		this.associateOtp = associateOtp;
	}

	public Boolean getAssociateOtpVerified() {
		return associateOtpVerified;
	}

	public void setAssociateOtpVerified(Boolean associateOtpVerified) {
		this.associateOtpVerified = associateOtpVerified;
	}

	@ManyToOne
	private Associate jugaadAssociate;

	public Associate getJugaadAssociate() {
		return jugaadAssociate;
	}

	public void setJugaadAssociate(Associate jugaadAssociate) {
		this.jugaadAssociate = jugaadAssociate;
	}

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public Role getCancelledByStatus() {
		return cancelledByStatus;
	}

	public void setCancelledByStatus(Role cancelledByStatus) {
		this.cancelledByStatus = cancelledByStatus;
	}

	@ManyToOne
	@JsonIgnore
	private StaffAvailabilitySlot staffAvailabilitySlot;

	public StaffAvailabilitySlot getStaffAvailabilitySlot() {
		return staffAvailabilitySlot;
	}

	public void setStaffAvailabilitySlot(StaffAvailabilitySlot staffAvailabilitySlot) {
		this.staffAvailabilitySlot = staffAvailabilitySlot;
	}

	public AppUser getCancelledBy() {
		return cancelledBy;
	}

	public void setCancelledBy(AppUser cancelledBy) {
		this.cancelledBy = cancelledBy;
	}

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}

	public HotelAssociate getHotelAssociate() {
		return hotelAssociate;
	}

	public void setHotelAssociate(HotelAssociate hotelAssociate) {
		this.hotelAssociate = hotelAssociate;
	}

	private OrderStatus status;

	private Boolean isVendorApprooved = false;

	private Boolean isCustomerApprooved = false;

	private Boolean sendOrderNotification = true;

	public Boolean getSendOrderNotification() {
		return sendOrderNotification;
	}

	public void setSendOrderNotification(Boolean sendOrderNotification) {
		this.sendOrderNotification = sendOrderNotification;
	}

	private TypeOfSearch typeOfsearch;

	private Float paidAmount;

	private Long totalAmount;

	private PaymentMode paymentMode;

	@Column(columnDefinition = "TEXT")
	private String reason;

	@OneToOne
	private Feedback feedback;

	@ManyToOne
	private AppUser cancelledBy;

	@ManyToOne
	private ServiceSubCategory serviceSubCategory;

	@OneToOne
	private FileEntity orderImage;

	@Column(columnDefinition = "TEXT")
	private String preferences;

	private String tempCustomerName;
	
	private String tempCustomerMobile;

	public String getPreferences() {
		return preferences;
	}
	public void setPreferences(String preferences) {
		this.preferences = preferences;
	}

	public FileEntity getOrderImage() {
		return orderImage;
	}

	public void setOrderImage(FileEntity orderImage) {
		this.orderImage = orderImage;
	}

	private Boolean isPersonalStarted = false;

	public Boolean getIsPersonalStarted() {
		return isPersonalStarted;
	}

	public void setIsPersonalStarted(Boolean isPersonalStarted) {
		this.isPersonalStarted = isPersonalStarted;
	}

	public ServiceSubCategory getServiceSubCategory() {
		return serviceSubCategory;
	}

	public void setServiceSubCategory(ServiceSubCategory serviceSubCategory) {
		this.serviceSubCategory = serviceSubCategory;
	}

	public PaymentMode getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(PaymentMode paymentMode) {
		this.paymentMode = paymentMode;
	}

	@OneToOne
	private VendorTransaction vendorTransaction;

	@ManyToOne
	private Address dropAddress;

	private String storeName;

	@JsonIgnore
	private CustomerOrder oldCustomerOrder;

	private TypeOfOrder orderType = TypeOfOrder.CUSTOMER;

	private OrderSubType orderSubType;

	private Role cancelledByStatus;
	
	private String verificationIdType;

	private String transactionId;
	
	
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getVerificationIdType() {
		return verificationIdType;
	}

	public void setVerificationIdType(String verificationIdType) {
		this.verificationIdType = verificationIdType;
	}
	public OrderSubType getOrderSubType() {

		return orderSubType;
	}

	public void setOrderSubType(OrderSubType orderSubType) {
		this.orderSubType = orderSubType;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public Address getDropAddress() {
		return dropAddress;
	}

	public void setDropAddress(Address dropAddress) {
		this.dropAddress = dropAddress;
	}

	public VendorTransaction getVendorTransaction() {
		return vendorTransaction;
	}

	public void setVendorTransaction(VendorTransaction vendorTransaction) {
		this.vendorTransaction = vendorTransaction;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	private Date orderDate;

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public TypeOfSearch getTypeOfsearch() {
		return typeOfsearch;
	}

	public void setTypeOfsearch(TypeOfSearch typeOfsearch) {
		this.typeOfsearch = typeOfsearch;
	}

	public Float getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(Float paidAmount) {
		this.paidAmount = paidAmount;
	}

	public Boolean getIsVendorApprooved() {
		return isVendorApprooved;
	}

	public void setIsVendorApprooved(Boolean isVendorApprooved) {
		this.isVendorApprooved = isVendorApprooved;
	}

	public Boolean getIsCustomerApprooved() {
		return isCustomerApprooved;
	}

	public void setIsCustomerApprooved(Boolean isCustomerApprooved) {
		this.isCustomerApprooved = isCustomerApprooved;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public String getOrderNumber() {
		return orderNumber;
	}

	@Override
	public Associate getAssociate() {
		return associate;
	}

	@Override
	public VendorServices getService() {
		return vendorServices;
	}

	@Override
	public Address getAddress() {
		return address;
	}

	public VendorServices getVendorServices() {
		return vendorServices;
	}

	public void setVendorServices(VendorServices vendorServices) {
		this.vendorServices = vendorServices;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
	public Date getFromTime() {
		return fromTime;
	}

	private String reassigned;
	
	private Integer invoiceAmount;

	public Integer getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(Integer invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public void setFromTime(Date fromTime) {
		final Calendar calendar = Calendar.getInstance();
		this.fromTime = fromTime;
		calendar.setTime(fromTime);
		if(vendorServices != null && vendorServices.getServiceSubCategory() != null && vendorServices.getServiceSubCategory().getNumberOfUnits() != null){
			calendar.add(Calendar.HOUR, Math.round(vendorServices.getServiceSubCategory().getNumberOfUnits()));
		}
		toTime = calendar.getTime();
	}

	@Override
	public Date getToTime() {
		return toTime;
	}

	public void setToTime(Date toTime) {
		this.toTime = toTime;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public void setAssociate(Associate associate) {
		this.associate = associate;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	@Override
	public OrderStatus getStatus() {
		return status;
	}

	public CallCustomer getCallCustomer() {
		return CallCustomer.find.where().eq("customer_order", id).findUnique();
	}

	

	public static Model.Finder<Long, CustomerOrder> find = new Finder<Long, CustomerOrder>(
			CustomerOrder.class);

	public static PagedList<CustomerOrder> page(int page, int pageSize,
			String sortBy, String order, String vendorId, int tab) {
		String where = " date(order_date) ";

		if (ReportType.CURRENT.ordinal() == tab) {
			where += " = CURRENT_DATE() ";
		} else if (ReportType.PAST.ordinal() == tab) {
			where += " < CURRENT_DATE() ";
		} else if (ReportType.FUTURE.ordinal() == tab) {
			where += " > CURRENT_DATE() ";
		}
		return find.where().orderBy("orderNumber").fetch("associate")
				.fetch("service").findPagedList(page, pageSize);

	}

	//@Override
	@Override
	public String getCustomerName() {
		if(customer != null && customer.getAppUser() != null){
			return customer.getAppUser().getName();
		}else{
			return tempCustomerName;
		}
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Feedback getFeedback() {
		return feedback;
	}

	public void setFeedback(Feedback feedback) {
		this.feedback = feedback;
	}

	public CustomerOrder fromSlot(Long slotId,Long subServiceId) {
		final CustomerOrder order = new CustomerOrder();
		final StaffAvailabilitySlot slot = StaffAvailabilitySlot.find.byId(slotId);
		final Associate ass = slot.getAssociate();
		final Vendor v = ass.getVendor();
		final Long vsid = slot.getService().getId();
		if(subServiceId != null && subServiceId != 0){
			final VendorServices serv = VendorServices.find.where().eq("vendor",v).eq("serviceSubCategoryId",subServiceId).findList().get(0);
			order.setVendorServices(serv);
			order.setServiceSubCategory(serv.getServiceSubCategory());
			final float f = serv.getServiceSubCategory().getNumberOfUnits();
			int approxTime = (int) f;
			approxTime += 1;
			//	calendar.add(Calendar.HOUR, approxTime);
		}

		final Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(slot.getDate());
		order.setFromTime(calendar2.getTime());
		order.setAssociate(ass);
		order.setVendor(v);
		order.setCustomer(UserController.getLoggedInUser().getCustomer());
		order.setPaymentMode(PaymentMode.COD);
		order.setStatus(OrderStatus.WAITING);
		order.setIsCustomerApprooved(Boolean.FALSE);
		order.setIsVendorApprooved(Boolean.FALSE);

		order.setOrderDate(new Date());
		final int count = CustomerOrder.find.all().size();

		final int year = calendar2.get(Calendar.YEAR);
		final int month = calendar2.get(Calendar.MONTH);
		/*	Logger.info("Month : " + month + " current month : "
				+ cal.get(Calendar.MONTH));*/
		order.setOrderNumber(year + "-" + (month+1) + "-" + (count+1));
		order.setStaffAvailabilitySlot(slot);
		final Calendar calendar = Calendar.getInstance();

		//order.setToTime(calendar.getTime());

		Customer customer = null;
		if (UserController.getLoggedInUser().getRole().equals(Role.CUSTOMER)) {
			customer = UserController.getLoggedInUser().getCustomer();
			order.setCustomer(customer);
		}

		return order;
	}

	@Override
	public String toString() {

		/*
		return "CustomerOrder [id=" + id + ", orderNumber=" + orderNumber + ", associate=" + associate
				+ ", vendorServices=" + vendorServices + ", customer=" + customer + ", vendor=" + vendor + ", fromTime="
				+ fromTime + ", toTime=" + toTime + ", address=" + address + ", staffAvailabilitySlot="
				+ staffAvailabilitySlot + ", status=" + status + ", isVendorApprooved=" + isVendorApprooved
				+ ", isCustomerApprooved=" + isCustomerApprooved + ", typeOfsearch=" + typeOfsearch + ", paidAmount="
				+ paidAmount + ", paymentMode=" + paymentMode + ", reason=" + reason + ", feedback=" + feedback
				+ ", cancelledBy=" + cancelledBy + ", vendorTransaction=" + vendorTransaction + ", orderDate="
				+ orderDate + "]";

		 */

		return "";
	}


	public static Map<Long,Map<Integer,CustomerOrder>> getTodaysOrderMap(Vendor vendor){
		final Map<Long,Map<Integer,CustomerOrder>> map = new HashMap<>();
		final Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);




		//Logger.info("date2   : "+calendar.getTime());
		final Calendar tomorrow = Calendar.getInstance();
		tomorrow.setTime(calendar.getTime());
		tomorrow.add(Calendar.DATE, 1);
		final List<CustomerOrder> orders = CustomerOrder.find.where().eq("vendor", vendor).ge("fromTime", calendar.getTime()).le("fromTime", tomorrow.getTime()).findList();
		for(final CustomerOrder order :  orders){
			// Logger.info(Integer.parseInt(new SimpleDateFormat("HH").format(order.fromTime))+" from time");
			//if(order.getStaffAvailabilitySlot()!=null) {
			if(order.getAssociate()==null || order.getAssociate().getAppUser()==null || order.getAssociate().getAppUser().getName()==null) {
				continue;
			}
			if(map.containsKey(order.getAssociate().getId())) {
				map.get(order.getAssociate().getId()).put(Integer.parseInt(new SimpleDateFormat("HH").format(order.fromTime)),order);
			} else {
				final Map<Integer,CustomerOrder> om = new HashMap<>();
				om.put(Integer.parseInt(new SimpleDateFormat("HH").format(order.fromTime)),order);
				map.put(order.getAssociate().getId(), om);
			}

			//  }

		}

		return map;
	}


	public static Map<Integer,CustomerOrder> getTodaysOrders(Long associateId){
		final Map<Integer,CustomerOrder> map = new HashMap<Integer,CustomerOrder>();
		final Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		//Logger.info("date2   : "+calendar.getTime());
		final Calendar tomorrow = Calendar.getInstance();
		tomorrow.setTime(calendar.getTime());
		tomorrow.add(Calendar.DATE, 1);

		for(final CustomerOrder order :  CustomerOrder.find.where().eq("associate", Associate.find.byId(associateId)).ge("fromTime", calendar.getTime()).le("fromTime", tomorrow.getTime()).findList()){
			//			   Logger.info(Integer.parseInt(new SimpleDateFormat("HH").format(order.fromTime))+" from time");
			//if(order.getStaffAvailabilitySlot()!=null) {
			map.put(Integer.parseInt(new SimpleDateFormat("HH").format(order.fromTime)),order);
			//  }

		}

		return map;
	}

	public static List<CustomerOrder> getPendingJobs(Long associateId){
		return CustomerOrder.find.where().eq("associate", Associate.find.byId(associateId)).eq("status", OrderStatus.WAITING).orderBy().asc("createdOn").findList();
	}

	/**Vendor orders utility methods for vendortranscation*/
	public static Map<String,Date> getMonthStartAndEndDate(){
		final Map<String,Date> map = new HashMap<String,Date>();
		final Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		calendar.getActualMinimum(Calendar.MONTH);

		final Calendar end = Calendar.getInstance();
		//end.setTime(calendar.getTime());
		end.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		map.put("start", calendar.getTime());
		map.put("end",end.getTime());
		return map;
	}

	public static List<CustomerOrder> getServedOrders(Long VendorId){
		final Vendor vendor = Vendor.find.byId(VendorId);
		final Map<String,Date> monthMap = getMonthStartAndEndDate();
		List<CustomerOrder> orderList = CustomerOrder.find.where().eq("vendor", vendor).eq("status", OrderStatus.COMPLETED).eq("vendorTransaction.isTransactionSetteled",false).ge("fromTime",monthMap.get("start")).le("fromTime",monthMap.get("end")).orderBy().asc("createdOn").findList();
		//Logger.info("Order List : "+orderList.size()+" vendorId "+vendor.getId());
		//Logger.info("start : "+monthMap.get("start")+" end "+monthMap.get("end"));
		return orderList;
	}
	public static List<CustomerOrder> getCancelledOrders(Long vendorId){
		final Vendor vendor = Vendor.find.byId(vendorId);
		final Map<String,Date> monthMap = getMonthStartAndEndDate();
		//Logger.info("start : "+monthMap.get("start")+" end "+monthMap.get("end"));
		return CustomerOrder.find.where().eq("vendor", vendor)
				.or(Expr.eq("status", OrderStatus.CANCELLED), Expr.eq("status", OrderStatus.DECLINED))
				.ge("fromTime",monthMap.get("start"))
				.le("fromTime",monthMap.get("end"))
				//.in("cancelledByStatus",Role.VENDOR,Role.VENDOR_STAFF)
				.orderBy().asc("createdOn").findList();
	}

	public static int getTotalAmount(Long VendorId,String paymenyMode){
		Float total = 0F;
		final Vendor vendor = Vendor.find.byId(VendorId);
		final Map<String,Date> monthMap = getMonthStartAndEndDate();
		final List<CustomerOrder> orderList = CustomerOrder.find.where().eq("vendor", vendor)
				.eq("status", OrderStatus.COMPLETED)
				.ne("vendorTransaction", null).eq("paymentMode", PaymentMode.valueOf(paymenyMode)).eq("vendorTransaction.isTransactionSetteled",false).ge("fromTime",monthMap.get("start")).le("fromTime",monthMap.get("end")).orderBy().asc("createdOn").findList();
		//Logger.info("sdgfsj == "+orderList.size());
		for(final CustomerOrder order : orderList){
			if(order.getVendorTransaction() != null && order.getVendorTransaction().getInvoiceAmount() != null){
				total = total+order.getVendorTransaction().getInvoiceAmount();
			}

		}
		return Math.round(total);
	}
	public static Integer totalVendorRevenue(Long vendorId){
		Integer total = 0;
		Integer percentageAmount = 0;
		final Vendor vendor = Vendor.find.byId(vendorId);
		final Map<String,Date> monthMap = getMonthStartAndEndDate();
		final List<CustomerOrder> orderList = CustomerOrder.find.where().eq("vendor", vendor)
				.eq("status", OrderStatus.COMPLETED)
				.ne("vendorTransaction", null).eq("vendorTransaction.isTransactionSetteled",false)
				.ge("fromTime",monthMap.get("start")).le("toTime",monthMap.get("end"))
				.orderBy().asc("createdOn").findList();

		for(CustomerOrder order : orderList){
			if(order.getVendorTransaction() != null && order.getVendorTransaction().getActualAmount() != null && order.getVendorTransaction().getInvoiceRevenue() != null) {
				if(order.getOrderType().equals(TypeOfOrder.CUSTOMER)) {
					total = (int) (total + order.getVendorTransaction().getActualAmount());
				} else if(order.getVendorTransaction().getAdditionalCharges() != null){
					total = (int) (total + order.getVendorTransaction().getAdditionalCharges());
				}
				percentageAmount = percentageAmount + order.getVendorTransaction().getInvoiceRevenue();
			}
		}
		return total - percentageAmount;
		/*
		final int total =getTotalAmount(vendorId,"COD")+getTotalAmount(vendorId,"CC");
		if(vendor.getContract() != null &&vendor.getContract().getPercentage() != null){
			return (int)Math.round(total - vendor.getContract().getPercentage() / 100.0 * total);
		}else{
			return 0;
		}*/
	}

	public static Integer getMonthlyPenalty(Long vendorId){
		final Map<String,Date> monthMap = getMonthStartAndEndDate();
		final Vendor vendor = Vendor.find.byId(vendorId);
		final List<CustomerOrder> orderList = CustomerOrder.find.where().eq("vendor", vendor).eq("vendorTransaction.isTransactionSetteled",false).ge("fromTime",monthMap.get("start")).le("toTime",monthMap.get("end")).orderBy().asc("createdOn").findList();
		Integer total = 0;
		for(final CustomerOrder order : orderList){
			if(Penalty.find.where().eq("customerOrder", order).eq("isPaidToJugaad",false).findList().size() > 0){
				for(final Penalty penalty : Penalty.find.where().eq("customerOrder", order).eq("isPaidToJugaad",false).findList()){
					total = total+penalty.getAmount();
				}
			}
		}
		return total;
	}

	/*public static Integer totalRevenueTOJugaadCod(Long vendorId){
		Vendor vendor = Vendor.find.byId(vendorId);
		Float total = getTotalAmount(vendorId,"COD");

		return (int) ( total - ((vendor.getContract().getPercentage() / 100.0) * total));
	}

	public static Integer totalRevenueTOVendorCC(Long vendorId){
		Vendor vendor = Vendor.find.byId(vendorId);
		Float total = getTotalAmount(vendorId,"CC");
		Integer percentage = 100 - vendor.getContract().getPercentage();
		return (int) ( total - ((percentage / 100.0) * total));
	}*/

	public static Integer totalTransactionamount(Long vendorId){
		return (int) (getTotalAmount(vendorId,"COD")+getTotalAmount(vendorId,"CC"));

	}

	public static List<CustomerOrder> getTodaysOrders(){

		final Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		//Logger.info("date2   : "+calendar.getTime());
		final Calendar tomorrow = Calendar.getInstance();
		tomorrow.setTime(calendar.getTime());
		tomorrow.add(Calendar.DATE, 1);
		return CustomerOrder.find.fetch("associate").where().ge("fromTime", calendar.getTime()).le("fromTime", tomorrow.getTime()).findList();

	}

	public Boolean getIsTrackable(){
		//Logger.info("order time"+this.fromTime);
		final Calendar orderTime = Calendar.getInstance();
		orderTime.setTime(fromTime);
		orderTime.add(Calendar.MINUTE, -30);
		//Logger.info("before 45 minutes "+orderTime.getTime());
		final Calendar now = Calendar.getInstance();
		//Logger.info("current time "+now.getTime());
		if(now.after(orderTime) && getStatus().equals(OrderStatus.WAITING)){
			//	Logger.info("after time");
			return true;
		}else{
			//	Logger.info("before time");
			return false;
		}
	}

	//	public List<CustomerOrderItems> getListCustomerIems() {
	//		return listCustomerIems;
	//	}
	//
	//	public void setListCustomerIems(List<CustomerOrderItems> listCustomerIems) {
	//		this.listCustomerIems = listCustomerIems;
	//	}

	public static List<CustomerOrder> getDeclinedOrders(){
		return CustomerOrder.find.where().eq("status",OrderStatus.DECLINED).orderBy().desc("lastUpdate").findList();
	}

	public static List<CustomerOrder> getDeclinedOrdersOnlyHousehold(){
		return CustomerOrder.find.where().eq("status",OrderStatus.DECLINED).or(Expr.eq("type_ofsearch",TypeOfSearch.DO_JUGAAD), Expr.eq("type_ofsearch",TypeOfSearch.INSTANT_JUGAAD)).orderBy().desc("lastUpdate").findList();
	}

	public Long getOrderItemTotalAmount(){
		Long price = 0l;
		if(vendorTransaction != null){
			price = vendorTransaction.getInvoiceAmount();
		}
		else if(this.getTotalAmount() != null){
			price = this.getTotalAmount();
		}
		else {
			final List<CustomerOrderItems> lcos = CustomerOrderItems.find.where().eq("customerOrder", this).findList();

			if(lcos != null){
				for(final CustomerOrderItems cos : lcos){
					price += cos.getAmount()==null?0l:cos.getAmount();
				}
			}
		}
		return price;
	}


	public Long getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Long totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getReassigned() {
		return reassigned;
	}

	public void setReassigned(String reassigned) {
		this.reassigned = reassigned;
	}

	public CustomerOrder getOldCustomerOrder() {
		return oldCustomerOrder;
	}

	public void setOldCustomerOrder(CustomerOrder oldCustomerOrder) {
		this.oldCustomerOrder = oldCustomerOrder;
	}

	public String getTempCustomerName() {
		return this.tempCustomerName;
	}

	public void setTempCustomerName(String tempCustomerName) {
		this.tempCustomerName = tempCustomerName;
	}

	public String getTempCustomerMobile() {
		return this.tempCustomerMobile;
	}

	public void setTempCustomerMobile(String tempCustomerMobile) {
		this.tempCustomerMobile = tempCustomerMobile;
	}

	public TypeOfOrder getOrderType() {
		return orderType;
	}

	public void setOrderType(TypeOfOrder orderType) {
		this.orderType = orderType;
	}

	/**
	 * Action to get order status to customer
	 */
	public String getCustomerOrderStatus(){
		if(status.equals(OrderStatus.WAITING) || status.equals(OrderStatus.ACCEPTED) || status.equals(OrderStatus.NOT_ACCEPTED) || status.equals(OrderStatus.NOT_DISPATCHED) || status.equals(OrderStatus.DISPATCHED) || status.equals(OrderStatus.DECLINED)){
			return OrderStatus.WAITING+"";
		}else if(status.equals(OrderStatus.EXPIRED) || status.equals(OrderStatus.CANCELLED)){
			return OrderStatus.CANCELLED+"";
		}
		return getStatus()+"";
	}

	/**
	 * Action to get vendor(Customer Order total Amount)
	 * @param VendorId
	 * @param paymenyMode
	 * @param monthMap
	 * @return
	 */
	public static int getCustomerOrderTotalAmount(Long VendorId){
		final Map<String,Date> monthMap = getMonthStartAndEndDate();
		Vendor vendor = Vendor.find.byId(VendorId);
		Float total = 0f;
		List<CustomerOrder> orderList = CustomerOrder.find.where().eq("vendor", vendor)
				.eq("status", OrderStatus.COMPLETED)
				.ne("vendorTransaction", null)
				.eq("orderType",TypeOfOrder.CUSTOMER)
				//	.eq("paymentMode", PaymentMode.valueOf(paymenyMode))
				.eq("vendorTransaction.isTransactionSetteled",false)
				.ge("fromTime",monthMap.get("start")).le("toTime",monthMap.get("end"))
				.orderBy().asc("createdOn").findList();
		for(CustomerOrder order : orderList){
			if(order.getVendorTransaction() != null && order.getVendorTransaction().getInvoiceAmount() != null){
				total = total+order.getVendorTransaction().getActualAmount();
			}

		}
		return Math.round(total);

	}


	/**
	 * Action to get vendor(Customer Order total Amount)
	 * @param VendorId
	 * @param paymenyMode
	 * @param monthMap
	 * @return
	 */
	public static int getVendorOrderTotalAmount(Long VendorId){
		final Map<String,Date> monthMap = getMonthStartAndEndDate();
		Vendor vendor = Vendor.find.byId(VendorId);
		Float total = 0f;
		List<CustomerOrder> orderList = CustomerOrder.find.where().eq("vendor", vendor)
				.eq("status", OrderStatus.COMPLETED)
				.ne("vendorTransaction", null)
				.eq("orderType",TypeOfOrder.VENDOR)
				//.eq("paymentMode", PaymentMode.valueOf(paymenyMode))
				.eq("vendorTransaction.isTransactionSetteled",false)
				.ge("fromTime",monthMap.get("start")).le("toTime",monthMap.get("end"))
				.orderBy().asc("createdOn").findList();
		for(CustomerOrder order : orderList){
			if(order.getVendorTransaction() != null && order.getVendorTransaction().getInvoiceAmount() != null){
				total = total+order.getVendorTransaction().getAdditionalCharges();
			}

		}
		return Math.round(total);

	}
	public String reservationForOrder(){
		String reservation = "";
		List<Reservation> reservationList = Reservation.find.where().eq("customerOrder", this).findList();
		if(reservationList.size() > 0){
			reservation = reservationList.get(0).getReservationNumber();
		}
		return reservation;
	}
	
	public Reservation getReservationForOrder(){
		Reservation reservation = null;
		List<Reservation> reservationList = Reservation.find.where().eq("customerOrder", this).findList();
		if(reservationList.size() > 0){
			reservation = reservationList.get(0);
		}
		return reservation;
	}
	
	/**
	 * Action to get Order status
	 */
	public OrderStatus getOrderStatus(){
		if(this.getVendorTransaction() != null || this.getStatus().equals(OrderStatus.COMPLETED)){
			return OrderStatus.COMPLETED;
		}else{
			return this.getStatus();
		}
	}
	public int getRoomNumber(Long customerId){
		CustomerHotelInfo info=CustomerHotelInfo.find.where().eq("customer_id", customerId).findUnique();
		int roomNo=info.getHotelCheckInInfo().getRoomNumber();
		return roomNo;
		
	}
	
}
