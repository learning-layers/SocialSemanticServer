package at.kc.tugraz.ss.adapter.rest;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ApiModel(value = "authCheckCred request parameter")
public class SSAuthCheckCredRESTPar extends SSInputPar{

    @XmlElement 
    @ApiModelProperty( value = "op", required = true )
    public String op;
    
    @XmlElement 
    @ApiModelProperty( value = "user", required = true )
    public String user;
    
    @XmlElement 
    @ApiModelProperty( value = "key", required = true )
    public String key;
    
    @XmlElement 
    @ApiModelProperty( value = "label", required = true )
    public String label;
    
    @XmlElement 
    @ApiModelProperty( value = "password", required = true )
    public String password;
}