package com.pmberjaya.indotiki.controllers;

import android.content.Context;


public class BaseController
{
  private final String DEFAULT_LANGUAGE = "id";
  protected final int DEFAULT_LIMIT = 10;
  private Context context;
  
  private BaseController() {}

  protected BaseController(Context paramContext)
  {
    this.context = paramContext;
  }
  
  public Context getContext()
  {
    return this.context;
  }

  public String imageUrlCleaner(String paramString)
  {
    return paramString.replaceAll("\\\\", "");
  }
}
