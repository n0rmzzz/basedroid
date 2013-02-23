package com.tinywebgears.basedroid.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.tinywebgears.basedroid.R;
import com.tinywebgears.basedroid.contentprovider.CommentsContentProvider;

/**
 * An activity representing a list of Items. This activity has different presentations for handset and tablets,
 * including an {@link ItemListFragment} and an {@link ItemDetailFragment} if it fits the screen.
 */
public class ItemListActivity extends SherlockFragmentActivity implements ItemListFragment.Callbacks,
        ItemDetailFragment.Callbacks
{
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        if (findViewById(R.id.item_detail_container) != null)
        {
            mTwoPane = true;
            // In two-pane mode, list items should be given the 'activated' state when touched.
            ((ItemListFragment) getSupportFragmentManager().findFragmentById(R.id.item_list))
                    .setActivateOnItemClick(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
            return false;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCommentSelected(long id)
    {
        Uri itemUri = Uri.parse(CommentsContentProvider.CONTENT_URI + "/" + id);
        if (mTwoPane)
        {
            // In two-pane mode, show the detail view in this activity by adding or replacing the detail fragment using
            // a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putParcelable(CommentsContentProvider.CONTENT_ITEM_TYPE, itemUri);
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment).commit();
        }
        else
        {
            // In single-pane mode, simply start the detail activity for the selected item ID.
            Intent detailIntent = new Intent(this, ItemDetailActivity.class);
            detailIntent.putExtra(CommentsContentProvider.CONTENT_ITEM_TYPE, itemUri);
            startActivity(detailIntent);
        }
    }

    @Override
    public void onCommentRemvoed()
    {
        ItemDetailFragment fragment = new ItemDetailFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment).commit();
    }
}
