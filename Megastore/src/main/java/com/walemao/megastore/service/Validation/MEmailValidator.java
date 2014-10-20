package com.walemao.megastore.service.Validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class MEmailValidator implements Validator {
	/*^			#start of the line
	  [_A-Za-z0-9-\\+]+	#  must start with string in the bracket [ ], must contains one or more (+)
	  (			#   start of group #1
	    \\.[_A-Za-z0-9-]+	#     follow by a dot "." and string in the bracket [ ], must contains one or more (+)
	  )*			#   end of group #1, this group is optional (*)
	    @			#     must contains a "@" symbol
	     [A-Za-z0-9-]+      #       follow by string in the bracket [ ], must contains one or more (+)
	      (			#         start of group #2 - first level TLD checking
	       \\.[A-Za-z0-9]+  #           follow by a dot "." and string in the bracket [ ], must contains one or more (+)
	      )*		#         end of group #2, this group is optional (*)
	      (			#         start of group #3 - second level TLD checking
	       \\.[A-Za-z]{2,}  #           follow by a dot "." and string in the bracket [ ], with minimum length of 2
	      )			#         end of group #3
	$			#end of the line*/
	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	private Pattern pattern;
	
	private Matcher matcher;
	
	
	
	public MEmailValidator() {
		pattern = Pattern.compile(EMAIL_PATTERN);
	}

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		
		checkEmail(target, errors);
	}

	
	private void checkEmail(Object target, Errors errors)
	{

	}
}
