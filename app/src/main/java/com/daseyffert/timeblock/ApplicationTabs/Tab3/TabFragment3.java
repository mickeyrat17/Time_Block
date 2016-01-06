package com.daseyffert.timeblock.ApplicationTabs.Tab3;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.daseyffert.timeblock.ApplicationTabs.Tab3.SingleNote.SingleNoteActivity;
import com.daseyffert.timeblock.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Daniel on 12/21/2015.
 * Note Taking Fragment
 */
public class TabFragment3 extends Fragment {

    private TextView mToDoTitleTextView;
    private ImageButton mAddNoteButton;
    private RecyclerView mNotesRecyclerView;
    private NoteAdapter mNoteAdapter;

    @Override
    public void onResume() {
        super.onResume();
        UpdateUI();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //Inflate the View
        View view = inflater.inflate(R.layout.fragment_notes_list, container, false);
        //Wire up views
        mToDoTitleTextView = (TextView) view.findViewById(R.id.fragment_notes_list_title);
        mAddNoteButton = (ImageButton) view.findViewById(R.id.fragment_notes_list_add);
        mNotesRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_notes_list_recycler_view);
        //Underline ToDoTitleTextView
        mToDoTitleTextView.setPaintFlags(mToDoTitleTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mToDoTitleTextView.setText("To-Do List");
        //setLayoutManager to Linear for RecyclerView
        mNotesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //Configure UserInterface
        UpdateUI();
        //Add listener to add button
        mAddNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotesItem note = new NotesItem();
                NotesSingleton.get(getActivity()).addNotesItem(note);
                Intent intent = SingleNoteActivity.newIntent(getActivity(), note.getId());
                startActivity(intent);
            }
        });
        return view;
    }

    private void UpdateUI() {
        NotesSingleton notesSingleton = NotesSingleton.get(getActivity());
        List<NotesItem> notes = notesSingleton.getNotesItems();

        //TODO figure out how to implement notifyDataSetChanged() without
        //TODO losing list
//        if (mNoteAdapter == null) {
            mNoteAdapter = new NoteAdapter(notes);
            mNotesRecyclerView.setAdapter(mNoteAdapter);
//        } else {
//            mNoteAdapter.notifyDataSetChanged();
//        }
    }

    /** CLASS
     * View Holder for each note item
     */
    private class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private NotesItem mNote;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageButton mDeleteButton;

        //Constructor sets up class by wiring widgets
        public NoteHolder(View itemView) {
            super(itemView);
            //Set onClick for the RecyclerView item
            itemView.setOnClickListener(this);
            //Wire up Widget Views for each RecyclerView Item
            mTitleTextView = (TextView) itemView.findViewById(R.id.notes_list_item_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.notes_list_item_date);
            mDeleteButton = (ImageButton) itemView.findViewById(R.id.notes_list_item_delete_button);
        }


        //Gets called in NoteAdapter.onBindViewHolder() in order to bind the
        //proper values to to Widget Views to a particular note
        public void bindNote(NotesItem note) {
            mNote = note;
            //Assign values to widgets
            mTitleTextView.setText(mNote.getTitle());
            mDateTextView.setText(formattedDate(mNote.getDate()));
            //TODO figure out how to delete view
            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    NotesSingleton.get(getActivity()).deleteNotesItem(mNote);
                    UpdateUI();

                }
            });
        }

        //Implementation of when the certain RecyclerView View is clicked
        @Override
        public void onClick(View view) {
            //pass mNote's Id to activity
            Intent intent = SingleNoteActivity.newIntent(getActivity(), mNote.getId());
            startActivity(intent);
        }

        //Format Date to dd/mm/yyyy from whatever date given
        private String formattedDate(Date date) {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            String formatD = format.format(date);
            return formatD;
        }
    }

    /** CLASS
     * Adapter used to help create the RecyclerView
     */
    private class NoteAdapter extends RecyclerView.Adapter<NoteHolder> {
        private List<NotesItem> mNotes;

        //Constructor class assigns list to local class listModel
        public NoteAdapter(List<NotesItem> notes) {
            mNotes = notes;
        }

        //Set up the layout of the RecyclerView items
        @Override
        public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            //Wire Up layout of ech item in recyclerView
            View view = inflater.inflate(R.layout.notes_list_item, parent, false);
            return new NoteHolder(view);
        }

        //Bind the viewHolder
        @Override
        public void onBindViewHolder(NoteHolder holder, int position) {
            NotesItem note = mNotes.get(position);

            //Set the values of the RecyclerView Item to those of the
            //particular note at the time
            holder.bindNote(note);
        }

        @Override
        public int getItemCount() {
            return mNotes.size();
        }
    }
}
