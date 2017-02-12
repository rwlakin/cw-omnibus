/***
  Copyright (c) 2008-2015 CommonsWare, LLC
  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain	a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
  by applicable law or agreed to in writing, software distributed under the
  License is distributed on an "AS IS" BASIS,	WITHOUT	WARRANTIES OR CONDITIONS
  OF ANY KIND, either express or implied. See the License for the specific
  language governing permissions and limitations under the License.
  
  From _The Busy Coder's Guide to Android Development_
    https://commonsware.com/Android
 */

package com.commonsware.android.snackbar.action;

import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ArrayAdapter;
import com.commonsware.cwac.crossport.design.widget.Snackbar;
import java.util.ArrayList;

public class AsyncDemoFragment extends ListFragment {
  private static final String[] items= { "lorem", "ipsum", "dolor",
      "sit", "amet", "consectetuer", "adipiscing", "elit", "morbi",
      "vel", "ligula", "vitae", "arcu", "aliquet", "mollis", "etiam",
      "vel", "erat", "placerat", "ante", "porttitor", "sodales",
      "pellentesque", "augue", "purus" };
  private ArrayList<String> model=new ArrayList<String>();
  private ArrayAdapter<String> adapter=null;
  private AddStringTask task=null;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setRetainInstance(true);

    adapter=
        new ArrayAdapter<String>(getActivity(),
            android.R.layout.simple_list_item_1,
            model);

    loadModel();
  }

  @Override
  public void onViewCreated(View v, Bundle savedInstanceState) {
    super.onViewCreated(v, savedInstanceState);

    getListView().setScrollbarFadingEnabled(false);
    setListAdapter(adapter);
  }

  @Override
  public void onDestroy() {
    if (task != null) {
      task.cancel(false);
    }

    super.onDestroy();
  }

  private void loadModel() {
    task=new AddStringTask();
    task.execute();
  }

  class AddStringTask extends AsyncTask<Void, String, Void> {
    @Override
    protected Void doInBackground(Void... unused) {
      for (String item : items) {
        if (isCancelled())
          break;
        
        publishProgress(item);
        SystemClock.sleep(400);
      }

      return(null);
    }

    @Override
    protected void onProgressUpdate(String... item) {
      if (!isCancelled()) {
        adapter.add(item[0]);
      }
    }

    @Override
    protected void onPostExecute(Void unused) {
      Snackbar munchie=Snackbar.make(getListView(), R.string.done,
          Snackbar.LENGTH_LONG);

      munchie.setAction(R.string.snackbar_action_restart,
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            adapter.clear();
            loadModel();
          }
        });

      munchie.show();

      task=null;
    }
  }
}
