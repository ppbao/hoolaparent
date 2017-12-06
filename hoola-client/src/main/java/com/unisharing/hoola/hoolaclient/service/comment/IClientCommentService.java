package com.unisharing.hoola.hoolaclient.service.comment;


import com.unisharing.hoola.hoolacommon.model.CommentModel;

public interface IClientCommentService {
	void insertContentComment(CommentModel commentModel);
	void deleteContentComment(CommentModel commentModel);

}
