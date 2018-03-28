package com.macbitsgoa.ard.viewholders;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.TextView;

import com.macbitsgoa.ard.R;
import com.macbitsgoa.ard.models.FaqItem;

/**
 * ViewHolder for {@link com.macbitsgoa.ard.models.FaqItem} used in
 * {@link com.macbitsgoa.ard.adapters.ForumAdapter}.
 *
 * @author Vikramaditya Kukreja
 */
public class FaqViewHolder extends RecyclerView.ViewHolder {

    private static float defaultCardElevation;

    /**
     * TextView to display question data.
     */
    private TextView questionTV;

    /**
     * Expandable text view to display answer data.
     */
    private TextView answerTV;

    /**
     * The whole itemView.
     */
    private CardView container;

    private boolean showAnswer = false;

    private int touchX;

    private int touchY;

    /**
     * Default constructor.
     *
     * @param itemView Nonnull inflated view.
     */
    public FaqViewHolder(@NonNull final View itemView) {
        super(itemView);
        bindViews(itemView);
        initListeners();
    }

    private void bindViews(final View itemView) {
        container = (CardView) itemView;
        questionTV = itemView.findViewById(R.id.tv_vh_fg_forum_general_question);
        answerTV = itemView.findViewById(R.id.tv_vh_fg_forum_general_answer);
        defaultCardElevation = ((CardView) itemView).getCardElevation();
    }

    private void initListeners() {
        container.setOnClickListener(v -> toggleAnswerVisibility());
        container.setOnTouchListener((v, event) -> {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                touchX = (int) event.getX();
                touchY = (int) event.getY() - questionTV.getHeight();
            }
            return false;
        });
    }

    public void populateView(final FaqItem item) {
        questionTV.setText(item.getQuestion());
        answerTV.setText(Html.fromHtml(item.getAnswer()));
    }

    private void toggleAnswerVisibility() {
        showAnswer = !showAnswer;
        ObjectAnimator animator;
        if (showAnswer) {
            animator = ObjectAnimator.ofFloat(container, "cardElevation", defaultCardElevation, 2 * defaultCardElevation);
        } else {
            animator = ObjectAnimator.ofFloat(container, "cardElevation", 2 * defaultCardElevation, defaultCardElevation);
        }
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (!showAnswer) {
                    float width = answerTV.getWidth();
                    float height = answerTV.getHeight();
                    float r = (float) Math.hypot(width, height);
                    Animator anim = ViewAnimationUtils.createCircularReveal(answerTV, touchX, touchY, r, 0);
                    anim.start();
                    anim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            answerTV.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (showAnswer) {
                    answerTV.setVisibility(View.VISIBLE);
                    float width = answerTV.getWidth();
                    float height = answerTV.getHeight();
                    float r = (float) Math.hypot(width, height);
                    Animator anim = ViewAnimationUtils.createCircularReveal(answerTV, touchX, 0, 0, r);
                    anim.start();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
        /*container.setCardElevation(showAnswer ? 2 * defaultCardElevation : defaultCardElevation);*/
    }
}
