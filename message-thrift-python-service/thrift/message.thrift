namespace java com.contains.thrift.message
namespace py messsage.api

service MessageService {
    bool sendMobileMessage(1:string mobile, 2:string message);

    bool sendEmailMessage(1:string email, 2:string message);

}