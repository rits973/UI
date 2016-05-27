package controllers;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import actions.BasicAuth;
import models.AppUser;
import models.Gallery;
import models.GalleryFileEntityInfo;
import models.Notification;
import models.Role;
import models.ServiceSubCategory;
import models.TypeOfLogin;
import models.Vendor;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import util.Alert;
import util.Forms;
import util.SMSService;
import util.SessionProperties;


public class UserController extends Controller{

	public Result signUpForm(){
		return ok(views.html.publicPage.signup.render());
	}
	/*public static Vendor getLoggedInVendor(){
		String vendorKey = session("loginId");
		if(vendorKey != null){
        Vendor vendor = Vendor.find.byId(Long.parseLong(session("loginId")));
        return vendor;
		}
        return null;
	}*/
	public static Boolean isLoggedIn(){
		if(session(SessionProperties.LOGGED_IN_USER_ID.trim()) != null && session(SessionProperties.LOGGED_IN_USER_ID.trim()) != ""){
			return true;
		}
		return false;
	}

	public static AppUser getLoggedInUser(){
		if(UserController.isLoggedIn()){
			return AppUser.find.byId(Long.parseLong(session(SessionProperties.LOGGED_IN_USER_ID.trim())));
		} else {
			return null;
		}
	}

	public static Role getLoggedInUserRole(){
		if(session(SessionProperties.LOGGED_IN_USER_ROLE) != null && session(SessionProperties.LOGGED_IN_USER_ROLE).trim() != ""){
			return Role.valueOf(session(SessionProperties.LOGGED_IN_USER_ROLE.trim()));
		}
		return null;
	}

	/**
	 * @author Lakshmi
	 * Action to get Customer signup type (FB,GOOGLE,MANUAL)
	 * @return
	 */
	public Result signupTypeForm(){
		return ok(views.html.customer.signupType.render());
	}
	/**
	 * @author Lakshmi
	 * Action to get Customer signup page
	 * @return
	 */
	public Result signupForm(String userName,String email,String socialSite){
		Map<String,String> map = new HashMap<String,String>();
		if(!userName.trim().isEmpty()){
			map.put("NAME", userName);
		}
		if(!email.trim().isEmpty()){
			map.put("EMAIL", email);
		}
		if(!socialSite.trim().isEmpty()){
			map.put("SOCIALSITE", socialSite);
		}
		return ok(views.html.customer.signup.render(map));
	}
	/**
	 * Action to get signup page of user
	 * @param role
	 * @return
	 */
	public Result getSignupForm(String role){
		Role userRole = Role.valueOf(role);
		if(userRole.equals(Role.VENDOR)){
			return ok(
					views.html.vendor.signupVendor.render(Forms.appUserForm)
					);
		}
		if(userRole.equals(Role.CUSTOMER)){
			return ok(
					views.html.customer.signup.render(new HashMap())
					);
		}
		else{
			return TODO;
		}
	}


	/**
	 * Action to get dashboard
	 */

	@BasicAuth
	public Result dashboard1(String lasturl){
		AppUser appUser = UserController.getLoggedInUser();
		if(appUser != null){
			if(appUser.getRole().equals(Role.VENDOR)){
				return redirect(routes.VendorController.index());
			}
			if(appUser.getRole().equals(Role.CUSTOMER)){
				if(lasturl == null || lasturl.trim().isEmpty()){
					return redirect(controllers.customer.routes.CustomerController.dashboard());} else {
						return redirect( URLDecoder.decode(lasturl));
					}
			}
			if(appUser.getRole().equals(Role.JUGAAD_ADMIN) || appUser.getRole().equals(Role.ADMIN_STAFF)){
				return redirect(controllers.admin.routes.JugaadAdminController.dashboard());
			}
			if(appUser.getRole().equals(Role.VENDOR_STAFF)){
				return redirect(routes.AssociateController.dashboard());
			}
			if(appUser.getRole().equals(Role.SUPERVISOR) || appUser.getRole().equals(Role.SERVICE_SUPERVISOR)){
				return redirect(routes.SupervisorController.dashboard());
			}
			if(appUser.getRole().equals(Role.SERVICE_VENDOR)){
				return redirect(routes.ServiceCabController.dashboard());
			}
			if(appUser.getRole().equals(Role.HOTEL)){
				return redirect(controllers.hotel.routes.HotelController.dashboard());
			}
			if(appUser.getRole().equals(Role.HOTEL_CUSTOMER)){
				return redirect(controllers.hotel.routes.HotelCustomerController.dashboard());
			}
			if(appUser.getRole().equals(Role.Food_DELIVERY))
			{
				return redirect(routes.VendorController.index());
				/*return redirect(controllers.hotel.routes.HotelRestaurantController.dashboard());*/
			}
			if(appUser.getRole().equals(Role.ROOM_SERVICE)){
				if(appUser.getHotelAssociate().getAssociateType()==null)
					return redirect(controllers.hotel.routes.HotelAssociateController.gethotelRoomService());
				else
				if(appUser.getHotelAssociate().getAssociateType().equals("ROOM_CLEANING")){
					System.out.println("ROOM_CLEANING=======================");
					return redirect(controllers.hotel.routes.HotelAssociateController.getRoomServiceAssociates(appUser.getHotelAssociate().getId()));
				}
				if(appUser.getHotelAssociate().getAssociateType().equals("LAUNDRY")){
					System.out.println("LAUNDRY=======================");
						return redirect(controllers.hotel.routes.HotelAssociateController.getRoomServiceAssociates(appUser.getHotelAssociate().getId()));
				}
			    if(appUser.getHotelAssociate().getAssociateType().equals("RE_FILL")){
			    	System.out.println("RE_FILL=======================");
							return redirect(controllers.hotel.routes.HotelAssociateController.getRoomServiceAssociates(appUser.getHotelAssociate().getId()));
			    }
				
				   
			}
			if(appUser.getRole().equals(Role.CASHIER)){
				return redirect(controllers.hotel.routes.HotelAssociateController.gethotelRoomService());
			}
			if(appUser.getRole().equals(Role.RECEPTIONIST)){
				return redirect(controllers.hotel.routes.HotelAssociateController.gethotelRoomService());
			}
		}
		flash().put("alert",new Alert("alert-danger","Please login to continue.").toString());
		return redirect(routes.Application.index());

	}

	@BasicAuth
	public Result dashboard(){
		return dashboard1("");
	}

	/**
	 * Action to resend SMS confirmation code
	 */
	public Result resendSMSCode(Long appId){
		AppUser appUser = AppUser.find.byId(appId);
		SMSService.sendConfirmationSMS(appUser);
		flash().put("alert",new Alert("alert-success","SMS code sent to your registered Mobilenumber.").toString());
		return ok(views.html.publicPage.mobileVerification.render(appUser));
	}

	/**
	 * Action to check social network login/signup
	 */
	public Result socialMediaSignupOrLogin(){
		DynamicForm form = Form.form().bindFromRequest();
		Logger.info(form.toString());
		Logger.info("Form "+request().queryString());
		String email = form.get("email");
		String id = form.get("socialMediaId");
		Logger.info("social id : "+id);
		String type= form.get("type");
		String name = form.get("name");
		Map<String,String> map = new HashMap<String,String>();
		List<AppUser> appUserList = AppUser.find.where().eq("socialMediaId", id.trim()).findList();
		if(appUserList != null && appUserList.size() > 0){
			Logger.info("Entered Existed Inside Inside");
			session().remove(SessionProperties.LOGGED_IN_USER_ID);
			session().remove(SessionProperties.LOGGED_IN_USER_ROLE);
			session(SessionProperties.LOGGED_IN_USER_ID,appUserList.get(0).getId()+"");
			session(SessionProperties.LOGGED_IN_USER_ROLE,appUserList.get(0).getRole()+"");
			//return ok("Authenticated");
			map.put("Authenticated","true");
		}else{

			if(AppUser.find.where().eq("email",email.toLowerCase().trim()).findList().size() > 0){
				AppUser appUser = AppUser.find.where().eq("email",email.trim()).findList().get(0);
				if(appUser.getTypeOfLogIn() == null || appUser.getTypeOfLogIn().equals(TypeOfLogin.MANUAL)){
					map.put("EXISTED","Jugaad" );
				}else{
					map.put("EXISTED",appUser.getTypeOfLogIn().capitalize() );
				}
			}else{
				map.put("email", email);
				map.put("type", type);
				map.put("name", name);
				map.put("id",id);
			}
			Logger.info(map.toString());

		}
		return ok(play.libs.Json.toJson(map));
		//		return ok(form.toString());
	}
	
	/**
	 * Action to get Vendor unread Notifications
	 */
	public Result getVendorNotificationCount(){
		
		Vendor vendor = null;
		vendor = UserController.getLoggedInUser().getVendor();
		if(vendor != null && Notification.find.where().eq("vendor",vendor).eq("verified",false).findList().size() > 0){
		return ok(Notification.find.where().eq("vendor", vendor).eq("verified", false).findList().size()+"");
		}
		return ok("0");
	}
	
	public Result getNotificationSound(){
//		Alert.playSoundOnNewNotification();
//		NotificationSound notificationSound = new NotificationSound();
//		 notificationSound.play();
		return ok("OK");
	}
	/**
	 * Action to get Gallery images for sub services
	 * @param serviceSubCategoryId
	 * @return
	 */
	public Result galleryImages(Long serviceSubCategoryId){
			ServiceSubCategory serviceSubCategory = null;
		if(serviceSubCategoryId!=null && serviceSubCategoryId!=0l){
			serviceSubCategory = ServiceSubCategory.find.byId(serviceSubCategoryId);
		}
		//Logger.info(serviceSubCategory.getId()+" categopry id");
		List<GalleryFileEntityInfo> galleryFileEntityInfos = new ArrayList<GalleryFileEntityInfo>();
		if(serviceSubCategory != null){
			Gallery gallery = Gallery.find.where().eq("serviceSubCategory",serviceSubCategory).findUnique();
			//Logger.info("gallery id = "+gallery.getId());
			if(gallery != null){
				galleryFileEntityInfos.addAll(GalleryFileEntityInfo.find.where().eq("gallery",gallery).findList());	
			}
		}
		return ok(views.html.publicPage.galleryTemplate.render(galleryFileEntityInfos));
		}
}
