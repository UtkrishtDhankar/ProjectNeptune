package utkrishtdhankar.projectneptune;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Shreyak Kumar on 28-10-2016.
 *
 * Cards Adapter adapts a list of cards to display a bunch of tasks
 */
public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.TaskCardViewHolder> {

    Fragment fragment;
    String callingFragment;

    /**
     * Class to hold a single Card instance.
     * Contains references to all the text views etc. inside the card
     * Is used to set these views from code
     */
    public static class TaskCardViewHolder extends RecyclerView.ViewHolder {
        // The name of the oldtask
        public TextView nameTextView;

        // The status (Inbox, Waiting, etc. for this oldtask)
        public TextView statusTextView;

        // The context for this oldtask (Home, etc.)
        public TextView contextTextView;

        // The cardview for this oldtask
        public CardView cardView;

        /**
         * Sets the different views to their values for the view that was passed in
         * @param view
         */
        public TaskCardViewHolder(View view) {
            super(view);

            cardView = (CardView) view.findViewById(R.id.card_view);
            nameTextView = (TextView) view.findViewById(R.id.info_text);
            statusTextView = (TextView) view.findViewById(R.id.status_text);
            contextTextView = (TextView) view.findViewById(R.id.task_context_text);
        }
    }

    // The dataset that this adapter will inflate
    // Contains all the tasks for this view
    private ArrayList<Task> dataset;

    /**
     * Constructs a new adapter, setting the dataset to the one supplied
     * @param newDataset The dataset to be inflated
     */
    public CardsAdapter(ArrayList<Task> newDataset) {
        this.dataset = newDataset;
    }

    /**
     * When the Home fragment needs cards
     * @param newDataset The array list of tasks to be inflated
     * @param homefrag The calling fragment reference (used to get a hold of assets etc.)
     */
    public CardsAdapter(ArrayList<Task> newDataset,AllTaskFragment homefrag) {
        callingFragment = "All Tasks";
        this.fragment = homefrag;
        this.dataset = newDataset;
    }

    /**
     * When the Home fragment needs cards
     * @param newDataset The array list of tasks to be inflated
     * @param nextfrag The calling fragment reference (used to get a hold of assets etc.)
     */
    public CardsAdapter(ArrayList<Task> newDataset,InboxFragment nextfrag) {
        callingFragment = "Inbox";
        this.fragment = nextfrag;
        this.dataset = newDataset;
    }

    /**
     * When the Home fragment needs cards
     * @param newDataset The array list of tasks to be inflated
     * @param nextfrag The calling fragment reference (used to get a hold of assets etc.)
     */
    public CardsAdapter(ArrayList<Task> newDataset,NextFragment nextfrag) {
        callingFragment = "Next";
        this.fragment = nextfrag;
        this.dataset = newDataset;
    }

    /**
     * When the Home fragment needs cards
     * @param newDataset The array list of tasks to be inflated
     * @param nextfrag The calling fragment reference (used to get a hold of assets etc.)
     */
    public CardsAdapter(ArrayList<Task> newDataset,WaitingFragment nextfrag) {
        callingFragment = "Waiting";
        this.fragment = nextfrag;
        this.dataset = newDataset;
    }

    /**
     * When the Home fragment needs cards
     * @param newDataset The array list of tasks to be inflated
     * @param nextfrag The calling fragment reference (used to get a hold of assets etc.)
     */
    public CardsAdapter(ArrayList<Task> newDataset,ScheduledFragment nextfrag) {
        callingFragment = "Scheduled";
        this.fragment = nextfrag;
        this.dataset = newDataset;
    }

    /**
     * When the Home fragment needs cards
     * @param newDataset The array list of tasks to be inflated
     * @param nextfrag The calling fragment reference (used to get a hold of assets etc.)
     */
    public CardsAdapter(ArrayList<Task> newDataset,SomedayFragment nextfrag) {
        callingFragment = "Someday";
        this.fragment = nextfrag;
        this.dataset = newDataset;
    }

    /**
     * Creates a new card and inflates it
     * Called by inflater
     * @param parent The parent view
     * @param viewType The viewType (used when multiple card types are to be inflated)
     * @return the viewHolder to be inflated
     */
    @Override
    public TaskCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);

        TaskCardViewHolder viewHolder = new TaskCardViewHolder(view);
        return viewHolder;
    }

    /**
     * Replaces the contents of holder at position with the contents from the dataset
     * Called by inflater
     * @param holder the holder for the card
     * @param position the position for this data in the dataset
     */
    @Override
    public void onBindViewHolder(TaskCardViewHolder holder, final int position) {

        // Importing the required fonts
        Typeface robotoLight = Typeface.createFromAsset(fragment.getActivity().getAssets(), "fonts/Roboto-Light.ttf");
        Typeface robotoLightItalic = Typeface.createFromAsset(fragment.getActivity().getAssets(), "fonts/Roboto-LightItalic.ttf");
        Typeface robotoRegular = Typeface.createFromAsset(fragment.getActivity().getAssets(), "fonts/Roboto-Regular.ttf");

        // Setting the fonts for all texts in the card
        holder.nameTextView.setTypeface(robotoRegular);
        holder.statusTextView.setTypeface(robotoLightItalic);
        holder.contextTextView.setTypeface(robotoRegular);

        // Getting elements from the dataset at this position
        // Replacing the contents of the elements
        holder.nameTextView.setText(dataset.get(position).getName());
        if(dataset.get(position).getStatus().getName().equals("Scheduled") || dataset.get(position).getStatus().getName().equals("Waiting")){
            holder.statusTextView.setText(dataset.get(position).getStatus().getName() +
                    " : " +
                    dataset.get(position).getStatus().getSpecial());
        }else {
            holder.statusTextView.setText(dataset.get(position).getStatus().getName());
        }

        if(dataset.get(position).getAllContexts().isEmpty()){
            holder.contextTextView.setText("");
        }else {

            // Setting the context's text
            holder.contextTextView.setText(" " + dataset.get(position).getAllContexts().get(0).getName() + " ");

            // Setting the context's background color
            holder.contextTextView.setBackgroundColor(dataset.get(position).getAllContexts().get(0).getColor());

            // Setting the text color black if background is white , else text color is white
            if (dataset.get(position).getAllContexts().get(0).getColor() == Color.parseColor("#ecf0f1")) {
                holder.contextTextView.setTextColor(Color.BLACK);
            } else {
                holder.contextTextView.setTextColor(Color.WHITE);
            }
        }

        // Setting the onClick listener for each card
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                FragmentManager fragmentManager = fragment.getFragmentManager();
                InputFragment inputFragment;
                if(dataset.get(position).getAllContexts().isEmpty()){
                    inputFragment = InputFragment.newInstance(callingFragment,
                            dataset.get(position).getName(),
                            "",
                            dataset.get(position).getStatus().encode(),
                            dataset.get(position).getStatus().getName(),
                            dataset.get(position).getStatus().getSpecial(),
                            dataset.get(position).getId());
                }else{
                    inputFragment = InputFragment.newInstance(callingFragment,
                            dataset.get(position).getName(),
                            dataset.get(position).getAllContexts().get(0).getName(),
                            dataset.get(position).getStatus().encode(),
                            dataset.get(position).getStatus().getName(),
                            dataset.get(position).getStatus().getSpecial(),
                            dataset.get(position).getId());
                }

                inputFragment.show(fragmentManager, "fragment_edit_name");
            }
        });


    }

    /**
     * Gets the number of items in the dataset
     * @return the number of items
     */
    @Override
    public int getItemCount() {
        if (dataset != null)
            return dataset.size();
        else
            return 0;
    }
}
